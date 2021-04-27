package com.mysport.sportapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RunningService: LifecycleService() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    var isFirstRun = true
    var serviceKilled = false

    private val timeRunInSeconds = MutableLiveData<Long>()

    private var isTrackerEnabled = false

    private var timeStarted = 0L
    private var lapTime = 0L
    private var lastSecondTimestamp = 0L
    private var timeRun = 0L

    companion object {
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val stepCount = MutableLiveData<Int>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        stepCount.postValue(0)
    }

    override fun onCreate() {
        super.onCreate()

        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()

        isTracking.observe(this, Observer {
            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constant.ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                        startTracker()
                    }
                }
                Constant.ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                Constant.ACTION_STOP_SERVICE -> {
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
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTrackerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            while(isTracking.value!!){


                delay(Constant.TRACKER_UPDATE_INTERVAL * 15)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInMillis.postValue(timeRun + lapTime)

                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }

                delay(Constant.TRACKER_UPDATE_INTERVAL)
            }

            timeRun += lapTime
        }
    }

    private fun startForegroundService() {
        startTracker()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // TODO: Customize notification
        startForeground(Constant.NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {
            if(!serviceKilled) {
                val notification = curNotificationBuilder
                        .setContentText(TrackerUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(Constant.NOTIFICATION_ID, notification.build())
            }
        })

        // TODO: Observe running
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTrackerEnabled = false
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                Constant.NOTIFICATION_CHANNEL_ID,
                Constant.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, CyclingService::class.java).apply {
                action = Constant.ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, CyclingService::class.java).apply {
                action = Constant.ACTION_START_OR_RESUME_SERVICE
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
            notificationManager.notify(Constant.NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }
}