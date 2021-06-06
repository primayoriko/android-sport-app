package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.domain.Training

@Dao
interface TrainingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(training: Training)

    @Delete
    suspend fun delete(training: Training)

    @Query("SELECT * FROM training WHERE id = :id")
    fun get(id: Int): LiveData<Training>

    @Query("SELECT * FROM training ORDER BY timestamp DESC")
    fun getAllSortedByDate(): LiveData<List<Training>>

    @Query("SELECT * FROM training WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getAllBetweenTimestamps(start: Long, end: Long): LiveData<List<Training>>

}