package com.mysport.sportapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import androidx.room.TypeConverter
import com.mysport.sportapp.data.Training

class Converter {
    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toTrainingType(value: String) = enumValueOf<Training.TrainingType>(value)

    @TypeConverter
    fun fromTrainingType(value: Training.TrainingType) = value.name
}