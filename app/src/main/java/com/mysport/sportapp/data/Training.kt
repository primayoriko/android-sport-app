package com.mysport.sportapp.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training")
data class Training(
    var type: TrainType = TrainType.CYCLING,
    var timestamp: Long = 0L,
    var duration: Float = 0F,
    var distance: Float? = null,
    var img: Bitmap? = null,
    var step: Int? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    enum class TrainType {
        CYCLING, RUNNING
    }
}


