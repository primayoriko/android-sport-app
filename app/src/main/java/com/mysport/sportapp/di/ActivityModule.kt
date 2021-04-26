package com.mysport.sportapp.di

import com.mysport.sportapp.db.dao.TrainingDao
import com.mysport.sportapp.repository.TrainingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

//@Module
//@InstallIn(ActivityComponent::class)
//abstract class ActivityModule {
//
//    @Binds
//    abstract fun provideTrainingRepository(trainingRepository: TrainingRepository): TrainingRepository
//
////    @Singleton
////    @Provides
////    fun provideTrainingRepository(
////            trainingDao: TrainingDao
////    ): TrainingRepository {
////        return TrainingRepository(trainingDao)
////    }
//}