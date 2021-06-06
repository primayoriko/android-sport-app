package com.mysport.sportapp.ui.main.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.repository.TrainingRepository

class HistoryViewModel @ViewModelInject constructor(
    val trainingRepository: TrainingRepository,
): ViewModel() {

    val trainingList = trainingRepository.getAllSortedByDate()

//    fun fetchOnADay(day: Int, month: Int, year: Int) {
//         val todayTimestamp = TimeUtility.getDayTimestampInMillis(day, month, year)
//         val tomorrowTimestamp = todayTimestamp + (1000 * 60 * 60 * 24)
//
//        val res = trainingRepository.getAllBetweenTimestamps(todayTimestamp, tomorrowTimestamp).value
//
//    }

}