package com.mysport.sportapp.di

import android.content.Context
import androidx.room.Room
import com.mysport.sportapp.constant.Constant.DATABASE_NAME
import com.mysport.sportapp.db.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainDatabase(
            @ApplicationContext app: Context
    ) = Room.databaseBuilder(
            app,
            MainDatabase::class.java,
            DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTrainingDao(db: MainDatabase) = db.getTrainingDao()

    @Singleton
    @Provides
    fun provideScheduleDao(db: MainDatabase) = db.getScheduleDao()

}