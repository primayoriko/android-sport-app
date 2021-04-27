package com.mysport.sportapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mysport.sportapp.data.Training

@Dao
interface TrainingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: Training)

    @Delete
    suspend fun deleteTraining(training: Training)

    // TODO: add fetch / querying method for spec no. 3

    // Examples
//    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
//    fun getAllRunsSortedByDate(): LiveData<List<Run>>
//
//    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
//    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Run>>
//

}