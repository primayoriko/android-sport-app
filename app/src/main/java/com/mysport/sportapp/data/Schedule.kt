package com.mysport.sportapp.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
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

}
