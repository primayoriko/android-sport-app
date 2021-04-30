package com.mysport.sportapp.ui.main.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.repository.TrainingRepository
import com.mysport.sportapp.util.TimeUtility
import java.util.*

class HistoryViewModel @ViewModelInject constructor(
    val trainingRepository: TrainingRepository
): ViewModel() {

    val trainingRecords: MutableLiveData<List<Training>> by lazy {
        MutableLiveData<List<Training>>()
    }

    val trainingList = trainingRepository.getAllSortedByDate()

    fun fetchOnADay(day: Int, month: Int, year: Int) {
         val todayTimestamp = TimeUtility.getDayTimestampInMillis(day, month, year)
         val tomorrowTimestamp = todayTimestamp + (1000 * 60 * 60 * 24)

        trainingRecords.value =
            trainingRepository.getAllBetweenTimestamps(todayTimestamp, tomorrowTimestamp).value
    }

}