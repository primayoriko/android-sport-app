package com.mysport.sportapp.ui.main.tracker

//import androidx.hilt.lifecycle.ViewModelInject
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var trainingRepository: TrainingRepository

    fun insertTraining(training: Training) = viewModelScope.launch {
        trainingRepository.insertTraining(training)

    }

}

//class TrackerViewModel @ViewModelInject constructor(
////        val trainingRepository: TrainingRepository
//): ViewModel() {
//
//    @Inject
//    lateinit var trainingRepository: TrainingRepository
//
//    fun insertTraining(training: Training) = viewModelScope.launch {
//        trainingRepository.insertTraining(training)
//
//    }
//
//}

//class TrackerViewModel @ViewModelInject constructor(
//        val trainingRepository: TrainingRepository
//): ViewModel() {
//
//    @Inject
//    lateinit var trainingRepository: TrainingRepository
//
//    fun insertTraining(training: Training) = viewModelScope.launch {
//        trainingRepository.insertTraining(training)
//
//    }
//
//}