package com.mysport.sportapp.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training")
data class Training(
    var type: TrainingType = TrainingType.CYCLING,
    var timestamp: Long = 0L,
    var duration: Float = 0F,
    var distance: Float? = null,
    var img: Bitmap? = null,
    var step: Int? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    enum class TrainingType {
        CYCLING, RUNNING
    }
}


