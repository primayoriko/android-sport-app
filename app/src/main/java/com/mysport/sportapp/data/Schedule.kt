package com.mysport.sportapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule(
   var title: String,
   var type: Training.TrainingType,
   var timestamp: Long = 0L,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
