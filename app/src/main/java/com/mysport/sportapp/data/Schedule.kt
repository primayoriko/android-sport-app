package com.mysport.sportapp.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mysport.sportapp.receiver.SchedulerBroadcastReceiver
import timber.log.Timber
import java.util.*


@Entity(tableName = "schedule")
data class Schedule(
        var title: String,
        var trainingType: Training.TrainingType,
        var scheduleType: ScheduleType = ScheduleType.EXACT,
        var hour: Int = 0,
        var minute: Int = 0,
        var durationInMinutes: Int = 0,
        var target: Int = 0,
        var isAutomated: Boolean = false,
        var isActive: Boolean = true,
        var notificationId: Int = 0,
        var day: Int = 0,
        var month: Int = 0,
        var year: Int = 0,
        var isMonday: Boolean = false,
        var isTuesday: Boolean = false,
        var isWednesday: Boolean = false,
        var isThursday: Boolean = false,
        var isFriday: Boolean = false,
        var isSaturday: Boolean = false,
        var isSunday: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    enum class ScheduleType {
        EXACT,
        ROUTINE
    }

    companion object {
        const val ID = "ID"
        const val TITLE = "TITLE"
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val FINISH_MSG = "FINISH_MSG"
    }

    fun invoke(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SchedulerBroadcastReceiver::class.java)
        val isRecurring = scheduleType == ScheduleType.ROUTINE

        intent.putExtra(ID, notificationId)
        intent.putExtra(TITLE, title)
        intent.putExtra(RECURRING, isRecurring)
        intent.putExtra(MONDAY, isMonday)
        intent.putExtra(TUESDAY, isTuesday)
        intent.putExtra(WEDNESDAY, isWednesday)
        intent.putExtra(THURSDAY, isThursday)
        intent.putExtra(FRIDAY, isFriday)
        intent.putExtra(SATURDAY, isSaturday)
        intent.putExtra(SUNDAY, isSunday)
        intent.putExtra(SUNDAY, isSunday)
        intent.putExtra(FINISH_MSG, false)

        val calendar: Calendar = Calendar.getInstance()
        var schedulerPendingIntent =
                PendingIntent.getBroadcast(context, notificationId, intent, 0)


        if(scheduleType == ScheduleType.EXACT){
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
        } else {
            calendar.timeInMillis = System.currentTimeMillis()
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            if(scheduleType == ScheduleType.EXACT){
                isActive = false
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
            }
        }

        if (!isRecurring) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC,
                        calendar.timeInMillis,
                        schedulerPendingIntent
                )

                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + durationInMinutes)
                intent.putExtra(FINISH_MSG, true)
                schedulerPendingIntent =
                        PendingIntent.getBroadcast(context, notificationId + 1, intent, 0)

                alarmManager.setExact(
                        AlarmManager.RTC,
                        calendar.timeInMillis,
                        schedulerPendingIntent
                )
            } else {
                Timber.d("ERROR: Can't create scheduler alarm, need API version above or equal to 19.")
            }
        } else {
            val runDaily = (24 * 60 * 60 * 1000).toLong()

            alarmManager.setRepeating(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    runDaily,
                    schedulerPendingIntent
            )

            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + durationInMinutes)
            intent.putExtra(FINISH_MSG, true)
            schedulerPendingIntent =
                    PendingIntent.getBroadcast(context, notificationId + 1, intent, 0)

            alarmManager.setRepeating(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    runDaily,
                    schedulerPendingIntent
            )
        }

//        this.started = true
    }

}
