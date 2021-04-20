package com.mysport.sportapp.repository

import javax.inject.Inject

import com.mysport.sportapp.data.Training
import com.mysport.sportapp.db.dao.TrainingDao

class TrainingRepository @Inject constructor(
    private val trainingDao: TrainingDao
) {
    suspend fun insertTraining(training: Training) = trainingDao.insertTraining(training)

    suspend fun deleteTraining(training: Training) = trainingDao.deleteTraining(training)

    // TODO: Link with all method in DAO

}