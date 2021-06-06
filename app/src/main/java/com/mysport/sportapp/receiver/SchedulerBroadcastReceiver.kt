package com.mysport.sportapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mysport.sportapp.domain.Schedule.Companion.AUTO_TRACK
import com.mysport.sportapp.domain.Schedule.Companion.DURATION
import com.mysport.sportapp.domain.Schedule.Companion.FINISH_MSG
import com.mysport.sportapp.domain.Schedule.Companion.FRIDAY
import com.mysport.sportapp.domain.Schedule.Companion.MESSAGE
import com.mysport.sportapp.domain.Schedule.Companion.MONDAY
import com.mysport.sportapp.domain.Schedule.Companion.RECURRING
import com.mysport.sportapp.domain.Schedule.Companion.SATURDAY
import com.mysport.sportapp.domain.Schedule.Companion.SUNDAY
import com.mysport.sportapp.domain.Schedule.Companion.THURSDAY
import com.mysport.sportapp.domain.Schedule.Companion.TUESDAY
import com.mysport.sportapp.domain.Schedule.Companion.WEDNESDAY
import com.mysport.sportapp.service.SchedulerService
import java.util.*


class SchedulerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            Toast.makeText(context, "Schedule changed", Toast.LENGTH_SHORT).show()
//            startReschedulerService(context)
        } else {
//            Toast.makeText(context, "Schedule triggered", Toast.LENGTH_SHORT).show()
            if (!intent.getBooleanExtra(RECURRING, false)) {
                startSchedulerService(context, intent)
            } else if (isScheduledToday(intent)) {
                startSchedulerService(context, intent)
            }
//            run {
//
//            }
        }
    }

    private fun isScheduledToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()

        when (calendar.get(Calendar.DAY_OF_WEEK)) {
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

        intentService.putExtra(MESSAGE, intent.getStringExtra(MESSAGE))
        intentService.putExtra(FINISH_MSG, intent.getBooleanExtra(FINISH_MSG, false))
        intentService.putExtra(AUTO_TRACK, intent.getBooleanExtra(AUTO_TRACK, false))
        intentService.putExtra(DURATION, intent.getIntExtra(DURATION, -1))

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(intentService)
        } else {
            context.startForegroundService(intentService)
        }
    }

//    private fun startReschedulerService(context: Context) {
//        val intentService = Intent(context, ReschedulerService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intentService)
//        } else {
//            context.startService(intentService)
//        }
//    }

}