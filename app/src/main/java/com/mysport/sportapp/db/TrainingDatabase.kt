package com.mysport.sportapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.db.dao.TrainingDao
import com.mysport.sportapp.util.Converter

@Database(
    entities = [Training::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class TrainingDatabase: RoomDatabase() {

    abstract fun getTrainingDao(): TrainingDao
}