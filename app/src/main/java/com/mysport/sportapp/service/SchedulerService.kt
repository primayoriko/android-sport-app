package com.mysport.sportapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mysport.sportapp.constant.Constant.SCHEDULER_NOTIFICATION_CHANNEL_ID
import com.mysport.sportapp.constant.Constant.SCHEDULER_NOTIFICATION_CHANNEL_NAME
import com.mysport.sportapp.constant.Constant.SCHEDULER_NOTIFICATION_CHANNEL_TITLE
import com.mysport.sportapp.constant.Constant.SCHEDULER_NOTIFICATION_ID
import com.mysport.sportapp.domain.Schedule.Companion.DURATION
import com.mysport.sportapp.domain.Schedule.Companion.FINISH_MSG
import com.mysport.sportapp.domain.Schedule.Companion.MESSAGE
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SchedulerService : Service() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

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

    private fun startForegroundServices(intent: Intent) {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        } else {
            Timber.d("ERROR: Can't create notification, need API version above or equal to 26.")
        }

//        Timber.d("eheheh\neheheh\neheheh\neheheh\neheheh\n")

        val message = intent.getStringExtra(MESSAGE)
        val isFinishMessage = intent.getBooleanExtra(FINISH_MSG, false)
        val duration = intent.getIntExtra(DURATION, -1)

        val notificationIntent = Intent(this, SchedulerService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0)
//            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pattern = longArrayOf(0, 150, 900)
        val notificationBuilder = baseNotificationBuilder
                .setChannelId(SCHEDULER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(SCHEDULER_NOTIFICATION_CHANNEL_TITLE)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
//                .setContentText(message)
                .setContentIntent(pendingIntent)
//        vibrator.vibrate(pattern, 0)
        vibrator.vibrate(800)
//        mediaPlayer.start()
        startForeground(SCHEDULER_NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                SCHEDULER_NOTIFICATION_CHANNEL_ID,
                SCHEDULER_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
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
//        notificationBuilder.javaClass.getDeclaredField("mActions").apply {
//            isAccessible = true
//            set(notificationBuilder, ArrayList<NotificationCompat.Action>())
//        }
//
//        if(!serviceKilled) {
//            notificationBuilder = baseNotificationBuilder
//                    .addAction(R.drawable.ic_baseline_pause_24, notificationActionText, pendingIntent)
//            notificationManager.notify(RUNNING_NOTIFICATION_ID, notificationBuilder.build())
//        }
}