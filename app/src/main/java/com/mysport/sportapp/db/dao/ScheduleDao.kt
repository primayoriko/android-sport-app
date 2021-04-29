package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.data.Schedule


@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    // TODO: Order by time or type
    @Query("SELECT * FROM schedule ORDER BY id DESC")
    fun getAllSortedByDate(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule ORDER BY id DESC")
    fun getAllSortedByID(): LiveData<List<Schedule>>

    @Update
    fun update(schedule: Schedule)

    // Examples
//    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
//    fun getAllRunsSortedByDate(): LiveData<List<Run>>

}