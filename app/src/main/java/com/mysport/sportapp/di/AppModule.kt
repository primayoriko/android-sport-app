package com.mysport.sportapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.mysport.sportapp.data.Constant.DATABASE_NAME
import com.mysport.sportapp.data.Constant.SHARED_PREFERENCES_NAME
import com.mysport.sportapp.db.TrainingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTrainingDatabase(
            @ApplicationContext app: Context
    ) = Room.databaseBuilder(
            app,
            TrainingDatabase::class.java,
            DATABASE_NAME
    )

    @Singleton
    @Provides
    fun provideTrainingDao(db: TrainingDatabase) = db.getTrainingDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
            app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    // Example Prefs Usage
//    @Singleton
//    @Provides
//    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""
//
//    @Singleton
//    @Provides
//    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)
//
//    @Singleton
//    @Provides
//    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
//            sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}