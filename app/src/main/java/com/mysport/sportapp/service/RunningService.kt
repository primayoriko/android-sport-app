package com.mysport.sportapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant.ACTION_PAUSE_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_START_OR_RESUME_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_STOP_SERVICE
import com.mysport.sportapp.data.Constant.RUNNING_NOTIFICATION_CHANNEL_ID
import com.mysport.sportapp.data.Constant.RUNNING_NOTIFICATION_CHANNEL_NAME
import com.mysport.sportapp.data.Constant.RUNNING_NOTIFICATION_ID
import com.mysport.sportapp.data.Constant.RUNNING_NOTIFICATION_CHANNEL_TITLE
import com.mysport.sportapp.data.Constant.TRACKER_UPDATE_INTERVAL
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class RunningService: LifecycleService(), SensorEventListener {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var curNotificationBuilder: NotificationCompat.Builder

    lateinit var sensorManager: SensorManager
    lateinit var stepDetectorSensor: Sensor
//    lateinit var stepCounterSensor: Sensor

    var isFirstTrack = true
    var serviceKilled = false

    private val timeTrackInSeconds = MutableLiveData<Long>()

    private var timeStarted = 0L
    private var lapTime = 0L
    private var lastSecondTimestamp = 0L
    private var timeTrack = 0L

    companion object {
        val timeTrainInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val stepCount = MutableLiveData<Int>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        timeTrackInSeconds.postValue(0L)
        timeTrainInMillis.postValue(0L)
        stepCount.postValue(0)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate() {
        super.onCreate()

        curNotificationBuilder = baseNotificationBuilder
        curNotificationBuilder
                .setContentTitle(RUNNING_NOTIFICATION_CHANNEL_TITLE)
                .setChannelId(RUNNING_NOTIFICATION_CHANNEL_ID)

        postInitialValues()

        isTracking.observe(this, Observer {
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

    override fun onDestroy() {
        super.onDestroy()

        serviceKilled = true
        Timber.d("Service Stopped")

    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onSensorChanged(event: SensorEvent) {

        if (isTracking.value!! && event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            val detectSteps = if (event.values[0].toInt() > 0) 1 else 0

            stepCount.postValue(stepCount.value!! + detectSteps)

        }

        // STEP_COUNTER Sensor.
        // *** Step Counting does not restart until the device is restarted - therefore, an algorithm for restarting the counting must be implemented.
//        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
//            val countSteps = event.values[0].toInt()
//
//            // -The long way of starting a new step counting sequence.-
//            /**
//             * int tempStepCount = countSteps;
//             * int initialStepCount = countSteps - tempStepCount; // Nullify step count - so that the step cpuinting can restart.
//             * currentStepCount += initialStepCount; // This variable will be initialised with (0), and will be incremented by itself for every Sensor step counted.
//             * stepCountTxV.setText(String.valueOf(currentStepCount));
//             * currentStepCount++; // Increment variable by 1 - so that the variable can increase for every Step_Counter event.
//             */
//
//            // -The efficient way of starting a new step counting sequence.-
//            if (stepCount.value!! == 0) { // If the stepCounter is in its initial value, then...
//                stepCount.postValue(event.values[0].toInt()) // Assign the StepCounter Sensor event value to it.
//            }
//
//            newStepCounter = countSteps - stepCounter // By subtracting the stepCounter variable from the Sensor event value - We start a new counting sequence from 0. Where the Sensor event value will increase, and stepCounter value will be only initialised once.
//        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startTracker() {
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeTrainInMillis.postValue(timeTrack + lapTime)

                if (timeTrainInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeTrackInSeconds.postValue(timeTrackInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }

                delay(TRACKER_UPDATE_INTERVAL)
            }

            timeTrack += lapTime
        }
    }

    private fun startForegroundService() {
        activateSensors()
        startTracker()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // TODO: Customize notification
        startForeground(RUNNING_NOTIFICATION_ID, baseNotificationBuilder.build())

        timeTrackInSeconds.observe(this, Observer {
            if (!serviceKilled) {
                val notification = curNotificationBuilder
                        .setContentText(TrackerUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(RUNNING_NOTIFICATION_ID, notification.build())
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

    private fun activateSensors(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
//        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorManager.registerListener(this, stepDetectorSensor, 0)
//        sensorManager.registerListener(this, stepCounterSensor, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                RUNNING_NOTIFICATION_CHANNEL_ID,
                RUNNING_NOTIFICATION_CHANNEL_NAME,
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
            notificationManager.notify(RUNNING_NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }
}