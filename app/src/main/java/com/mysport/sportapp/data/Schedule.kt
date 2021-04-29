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
        var trainType: Training.TrainingType,
        var scheduleType: ScheduleType = ScheduleType.DAY,
        var hour: Int = 0,
        var minute: Int = 0,
        var durationInMinutes: Int = 0,
        var target: Int = 0,
        var isRecurring: Boolean = false,
        var isMonday: Boolean = false,
        var isTuesday: Boolean = false,
        var isWednesday: Boolean = false,
        var isThursday: Boolean = false,
        var isFriday: Boolean = false,
        var isSaturday: Boolean = false,
        var isSunday: Boolean = false,
        var notificationId: Int = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    enum class ScheduleType {
        EXACT,
        DAY
    }

    companion object {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
    }

    fun invoke(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SchedulerBroadcastReceiver::class.java)

        intent.putExtra(TITLE, title)
        intent.putExtra(RECURRING, isRecurring)
        intent.putExtra(MONDAY, isMonday)
        intent.putExtra(TUESDAY, isTuesday)
        intent.putExtra(WEDNESDAY, isWednesday)
        intent.putExtra(THURSDAY, isThursday)
        intent.putExtra(FRIDAY, isFriday)
        intent.putExtra(SATURDAY, isSaturday)
        intent.putExtra(SUNDAY, isSunday)

        val alarmPendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0)
        val calendar: Calendar = Calendar.getInstance()

        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }

        if (!isRecurring) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmPendingIntent
                )
            } else {
                Timber.d("ERROR: Can't create scheduler alarm, need API version above or equal to 19.")
            }
        } else {
            val runDaily = (24 * 60 * 60 * 1000).toLong()

            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    runDaily,
                    alarmPendingIntent
            )
        }

//        this.started = true
    }

}
