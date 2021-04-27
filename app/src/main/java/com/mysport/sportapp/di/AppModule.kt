package com.mysport.sportapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.mysport.sportapp.data.Constant.DATABASE_NAME
import com.mysport.sportapp.db.TrainingDatabase
import com.mysport.sportapp.db.dao.TrainingDao
import com.mysport.sportapp.repository.TrainingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTrainingDatabase(
            @ApplicationContext app: Context
    ) = Room.databaseBuilder(
            app,
            TrainingDatabase::class.java,
            DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTrainingDao(db: TrainingDatabase) = db.getTrainingDao()

//    @Singleton
//    @Provides
//    fun provideTrainingRepository(
//            trainingDao: TrainingDao
//    ): TrainingRepository {
//        return TrainingRepository(trainingDao)
//    }

}