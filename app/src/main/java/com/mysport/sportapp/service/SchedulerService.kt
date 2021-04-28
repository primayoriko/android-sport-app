package com.mysport.sportapp.service

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.mysport.sportapp.data.Constant
import com.mysport.sportapp.data.Schedule.Companion.TITLE
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SchedulerService : Service() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
//    lateinit var curNotificationBuilder: NotificationCompat.Builder

    private lateinit var vibrator: Vibrator
//    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
//        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForegroundServices(intent)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        vibrator.cancel()
//        mediaPlayer.stop()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun startForegroundServices(intent: Intent) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        } else {
            Timber.d(" ERROR: Can't create notification, need API version above 26.")
        }

        val alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE))
        val notificationIntent = Intent(this, SchedulerService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0)
//            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = baseNotificationBuilder
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setContentIntent(pendingIntent)

        val pattern = longArrayOf(0, 100, 100)
        vibrator.vibrate(pattern, 6)
//        mediaPlayer.start()

        startForeground(Constant.RUNNING_NOTIFICATION_ID, notificationBuilder.build())

    }

//    val notificationActionText = if(isTracking) "Pause" else "Resume"
//        val pendingIntent = if(isTracking) {
//            val pauseIntent = Intent(this, CyclingService::class.java).apply {
//                action = ACTION_PAUSE_SERVICE
//            }
//
//            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        } else {
//            val resumeIntent = Intent(this, CyclingService::class.java).apply {
//                action = ACTION_START_OR_RESUME_SERVICE
//            }
//
//            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
//            isAccessible = true
//            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
//        }
//
//        if(!serviceKilled) {
//            curNotificationBuilder = baseNotificationBuilder
//                    .addAction(R.drawable.ic_baseline_pause_24, notificationActionText, pendingIntent)
//            notificationManager.notify(RUNNING_NOTIFICATION_ID, curNotificationBuilder.build())
//        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                Constant.RUNNING_NOTIFICATION_CHANNEL_ID,
                Constant.RUNNING_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }

}