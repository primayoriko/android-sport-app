package com.mysport.sportapp.ui.main.history

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.repository.TrainingRepository
import com.mysport.sportapp.util.TimeUtility
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HistoryViewModel @ViewModelInject constructor(
    val trainingRepository: TrainingRepository,
): ViewModel() {

    val trainingList = trainingRepository.getAllSortedByDate()

//    fun fetchOnADay(day: Int, month: Int, year: Int) {
//         val todayTimestamp = TimeUtility.getDayTimestampInMillis(day, month, year)
//         val tomorrowTimestamp = todayTimestamp + (1000 * 60 * 60 * 24)
//
//        Timber.d("hasileuy")
//        if(trainingRecords.value != null)Timber.d(trainingRecords.value!!.first().toString())
//        Timber.d("hasileuy")
//
//        val res = trainingRepository.getAllBetweenTimestamps(todayTimestamp, tomorrowTimestamp).value
//
//        Timber.d("hasileuy23")
////        if(res. != null)Timber.d(res.value!!.first().toString())
//        Timber.d("hasileuy23")
//
//    }

}