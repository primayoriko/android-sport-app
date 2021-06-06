package com.mysport.sportapp.domain

import android.graphics.Bitmap
import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mysport.sportapp.util.GraphicUtility

@Entity(tableName = "training")
data class Training(
    var type: TrainingType = TrainingType.CYCLING,
    var timestamp: Long = 0L,
    var duration: Long = 0L,
    var img: Bitmap = GraphicUtility.createImage(50, 50, Color.BLACK),
    var distance: Float? = null,
    var step: Int? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    enum class TrainingType {
        CYCLING, RUNNING
    }
}


