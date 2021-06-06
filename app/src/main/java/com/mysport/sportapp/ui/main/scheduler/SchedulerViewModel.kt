package com.mysport.sportapp.ui.main.scheduler

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysport.sportapp.domain.Schedule
import com.mysport.sportapp.repository.ScheduleRepository
import kotlinx.coroutines.launch

class SchedulerViewModel @ViewModelInject constructor(
        val scheduleRepository: ScheduleRepository
): ViewModel() {

//    @Inject
//    lateinit var scheduleRepository: ScheduleRepository
    val scheduleList: LiveData<List<Schedule>> =
            scheduleRepository.getAllSortedByID()

    fun insert(schedule: Schedule) =
            viewModelScope.launch {
                scheduleRepository.insert(schedule)
            }

    fun update(schedule: Schedule) =
            viewModelScope.launch {
                scheduleRepository.update(schedule)
            }

//    fun getAllSortedByDate() = scheduleList

}