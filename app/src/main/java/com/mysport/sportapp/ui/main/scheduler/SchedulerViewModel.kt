package com.mysport.sportapp.ui.main.scheduler

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.repository.ScheduleRepository
import kotlinx.coroutines.launch

class SchedulerViewModel  @ViewModelInject constructor(
        val scheduleRepository: ScheduleRepository
): ViewModel() {

//    @Inject
//    lateinit var scheduleRepository: ScheduleRepository
//    private var alarmsLiveData: LiveData<MutableList<Alarm?>?>? = null

    fun insert(schedule: Schedule) =
            viewModelScope.launch {
                scheduleRepository.insert(schedule)
            }

//    fun update(alarm: Alarm?) {
//        alarmRepository.update(alarm)
//    }

//    fun getAlarmsLiveData(): LiveData<List<Alarm?>?>? {
//        return alarmsLiveData
//    }

}