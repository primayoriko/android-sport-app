package com.mysport.sportapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.mysport.sportapp.data.Schedule.Companion.FRIDAY
import com.mysport.sportapp.data.Schedule.Companion.MONDAY
import com.mysport.sportapp.data.Schedule.Companion.RECURRING
import com.mysport.sportapp.data.Schedule.Companion.SATURDAY
import com.mysport.sportapp.data.Schedule.Companion.SUNDAY
import com.mysport.sportapp.data.Schedule.Companion.THURSDAY
import com.mysport.sportapp.data.Schedule.Companion.TITLE
import com.mysport.sportapp.data.Schedule.Companion.TUESDAY
import com.mysport.sportapp.data.Schedule.Companion.WEDNESDAY
import com.mysport.sportapp.service.SchedulerService
import timber.log.Timber
import java.util.*


class SchedulerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val toastText = String.format("Alarm Reboot")

            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

//            startRescheduleAlarmsService(context)

        } else {
            Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()

            if (!intent.getBooleanExtra(RECURRING, false)) {
                Timber.d("sadasd\nasdas\nasdsa")
                startSchedulerService(context, intent)
            }

            run {
                Timber.d("sad2asd\nas3das\nasd5sa")
                if (isScheduledToday(intent)) {
                    Timber.d("sad2a234sd\nas3da432s\nasd12135sa")
                    startSchedulerService(context, intent)
                }
            }
        }
    }

    private fun isScheduledToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        val today: Int = calendar.get(Calendar.DAY_OF_WEEK)

        calendar.timeInMillis = System.currentTimeMillis()

        when (today) {
            Calendar.MONDAY -> {
                return intent.getBooleanExtra(MONDAY, false)
            }
            Calendar.TUESDAY -> {
                return intent.getBooleanExtra(TUESDAY, false)
            }
            Calendar.WEDNESDAY -> {
                return intent.getBooleanExtra(WEDNESDAY, false)
            }
            Calendar.THURSDAY -> {
                return intent.getBooleanExtra(THURSDAY, false)
            }
            Calendar.FRIDAY -> {
                return intent.getBooleanExtra(FRIDAY, false)
            }
            Calendar.SATURDAY -> {
                return intent.getBooleanExtra(SATURDAY, false)
            }
            Calendar.SUNDAY -> {
                return intent.getBooleanExtra(SUNDAY, false)
            }
        }

        return false
    }

    private fun startSchedulerService(context: Context, intent: Intent) {
        val intentService = Intent(context, SchedulerService::class.java)

        intentService.putExtra(TITLE, intent.getStringExtra(TITLE))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.d("wqewqe\naswqeqweasd12wqewq")
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

//    private fun startReschedulerService(context: Context) {
//        val intentService = Intent(context, RescheduleAlarmsService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intentService)
//        } else {
//            context.startService(intentService)
//        }
//    }

}