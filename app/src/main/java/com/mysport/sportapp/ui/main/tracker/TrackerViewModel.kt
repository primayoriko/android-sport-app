package com.mysport.sportapp.ui.main.tracker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.repository.TrainingRepository
import kotlinx.coroutines.launch

class TrackerViewModel @ViewModelInject constructor(
    val trainingRepository: TrainingRepository
): ViewModel() {

//    @Inject
//    lateinit var trainingRepository: TrainingRepository

    fun insert(training: Training) =
            viewModelScope.launch {
                trainingRepository.insert(training)
            }

}

//class TrackerViewModel @ViewModelInject constructor(
////        val trainingRepository: TrainingRepository
//): ViewModel() {
//
//    @Inject
//    lateinit var trainingRepository: TrainingRepository
//
//    fun insert(training: Training) = viewModelScope.launch {
//        trainingRepository.insert(training)
//
//    }
//
//}
