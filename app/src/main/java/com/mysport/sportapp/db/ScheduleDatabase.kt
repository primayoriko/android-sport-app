package com.mysport.sportapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.db.dao.ScheduleDao
import com.mysport.sportapp.util.Converter

@Database(
        entities = [Schedule::class],
        version = 1
)
@TypeConverters(Converter::class)
abstract class ScheduleDatabase: RoomDatabase() {

    abstract fun getScheduleDao(): ScheduleDao
}