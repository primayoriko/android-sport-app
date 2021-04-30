package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.data.Training

@Dao
interface TrainingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(training: Training)

    @Delete
    suspend fun delete(training: Training)

    @Query("SELECT * FROM training WHERE id = :id")
    fun get(id: Int): LiveData<Training>

    @Query("SELECT * FROM training") //" WHERE date = :day")
    fun getAllOnADay(day: Int, month: Int, year: Int): LiveData<List<Training>>

    // Examples
//    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
//    fun getAllRunsSortedByDate(): LiveData<List<Run>>
//
//    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
//    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Run>>

}