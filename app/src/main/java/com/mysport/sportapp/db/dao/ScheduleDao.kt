package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.domain.Schedule


@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule)

    @Update
    suspend fun update(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun get(id: Int): LiveData<Schedule>

    @Query("SELECT * FROM schedule ORDER BY id DESC")
    fun getAllSortedByID(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule ORDER BY isActive DESC")
    fun getAllSortedByStatus(): LiveData<List<Schedule>>

    // TODO: Order by time or type
    @Query("SELECT * FROM schedule ORDER BY id DESC")
    fun getAllSortedByDate(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule WHERE isActive = 1 ORDER BY id DESC")
    fun getAllActive(): LiveData<List<Schedule>>

}