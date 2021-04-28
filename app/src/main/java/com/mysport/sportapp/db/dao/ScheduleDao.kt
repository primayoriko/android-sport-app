package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.data.Schedule

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedule ORDER BY timestamp DESC")
    fun getAllSchedulesSortedByDate(): LiveData<List<Schedule>>

    // Examples
//    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
//    fun getAllRunsSortedByDate(): LiveData<List<Run>>

}