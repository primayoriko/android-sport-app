package com.mysport.sportapp.repository

import javax.inject.Inject

import com.mysport.sportapp.data.Training
import com.mysport.sportapp.db.dao.TrainingDao

class TrainingRepository @Inject constructor(
    val trainingDao: TrainingDao
) {
    suspend fun insert(training: Training) = trainingDao.insert(training)

    suspend fun delete(training: Training) = trainingDao.delete(training)

    fun getAllSortedByDate() = trainingDao.getAllSortedByDate()

    fun getAllBetweenTimestamps(start: Long, end: Long) = trainingDao.getAllBetweenTimestamps(start, end)

}