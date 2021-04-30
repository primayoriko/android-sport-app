package com.mysport.sportapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.db.dao.ScheduleDao
import com.mysport.sportapp.db.dao.TrainingDao
import com.mysport.sportapp.util.Converter

@Database(
    entities = [Training::class, Schedule::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class MainDatabase: RoomDatabase() {

    abstract fun getTrainingDao(): TrainingDao
    abstract  fun getScheduleDao(): ScheduleDao
}