package com.mysport.sportapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

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
}
