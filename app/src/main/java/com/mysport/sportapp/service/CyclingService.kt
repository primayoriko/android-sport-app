package com.mysport.sportapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant
import com.mysport.sportapp.data.Constant.ACTION_PAUSE_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_START_OR_RESUME_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_STOP_SERVICE
import com.mysport.sportapp.data.Constant.CYCLING_NOTIFICATION_CHANNEL_ID
import com.mysport.sportapp.data.Constant.CYCLING_NOTIFICATION_CHANNEL_NAME
import com.mysport.sportapp.data.Constant.CYCLING_NOTIFICATION_ID
import com.mysport.sportapp.data.Constant.CYCLING_NOTIFICATION_CHANNEL_TITLE
import com.mysport.sportapp.data.Constant.FASTEST_LOCATION_INTERVAL
import com.mysport.sportapp.data.Constant.LOCATION_UPDATE_INTERVAL
import com.mysport.sportapp.data.Constant.TRACKER_UPDATE_INTERVAL
import com.mysport.sportapp.data.Polylines
import com.mysport.sportapp.util.PermissionUtility
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CyclingService: LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var curNotificationBuilder: NotificationCompat.Builder

    var isFirstTrack = true
    var serviceKilled = false

    private val timeRunInSeconds = MutableLiveData<Long>()

    private var timeStarted = 0L
    private var lapTime = 0L
    private var lastSecondTimestamp = 0L
    private var timeRun = 0L

    private var lastLocation: LatLng? = null

    companion object {
        val timeTrainInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val distanceInMeters = MutableLiveData<Float>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeTrainInMillis.postValue(0L)
        distanceInMeters.postValue(0F)
    }

    override fun onCreate() {
        super.onCreate()

        // TODO: Customize notification
        curNotificationBuilder = baseNotificationBuilder
        curNotificationBuilder
                .setContentTitle(Constant.CYCLING_NOTIFICATION_CHANNEL_TITLE)
                .setChannelId(Constant.CYCLING_NOTIFICATION_CHANNEL_ID)

        postInitialValues()

        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackerState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstTrack) {
                        startForegroundService()
                        isFirstTrack = false
                    } else {
                        Timber.d("Resuming service...")
                        startTracker()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    killService()
                }
                else -> {
                    Timber.d("Unknown command")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTracker() {
        addEmptyPolyline()

        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            val paths = pathPoints.value!!
            while(isTracking.value!!){
                if(paths.isNotEmpty()){
                    val path = paths.last()

                    if(paths.last().size > 1 && lastLocation != null){
                        val curLoc: LatLng = path.last()
                        val distances = FloatArray(1)

                        Timber.d("Last Loc: longi ${lastLocation!!.longitude} lati ${lastLocation!!.latitude}")
                        Timber.d("Cur Loc: longi ${curLoc.longitude} lati ${curLoc.latitude}")

                        Location.distanceBetween(
                                lastLocation!!.latitude,
                                lastLocation!!.longitude,
                                curLoc.latitude,
                                curLoc.longitude,
                                distances
                        )

                        distanceInMeters.postValue(distanceInMeters.value!! + distances[0]/3)

                        lastLocation = curLoc

                    } else if(path.isNotEmpty()) {
                        lastLocation = path.last()

                    }

                }

                delay(TRACKER_UPDATE_INTERVAL * 15)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeTrainInMillis.postValue(timeRun + lapTime)

                if (timeTrainInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }

                delay(TRACKER_UPDATE_INTERVAL)
            }

            timeRun += lapTime
        }
    }

    private fun startForegroundService() {
        startTracker()

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        } else {
            Timber.d(" ERROR: Can't create notification, need API version above 26.")
        }

        startForeground(CYCLING_NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {
            if(!serviceKilled) {
                val notification = curNotificationBuilder
                        .setContentText(TrackerUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(CYCLING_NOTIFICATION_ID, notification.build())
            }
        })

        // TODO: Observe running

    }

    private fun pauseService() {
        isTracking.postValue(false)

    }

    private fun killService() {
        serviceKilled = true
        isFirstTrack = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                CYCLING_NOTIFICATION_CHANNEL_ID,
                CYCLING_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotificationTrackerState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, CyclingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }

            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, CyclingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }

            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if(!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder
                    .addAction(R.drawable.ic_baseline_pause_24, notificationActionText, pendingIntent)
            notificationManager.notify(CYCLING_NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result!!)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)

                        Timber.d("New Location: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (PermissionUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                fusedLocationProviderClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

}