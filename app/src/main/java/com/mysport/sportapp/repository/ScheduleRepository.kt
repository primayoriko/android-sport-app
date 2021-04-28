package com.mysport.sportapp.repository

import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.db.dao.ScheduleDao
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
        val scheduleDao: ScheduleDao
) {
    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)

    suspend fun deleteSchedule(schedule: Schedule) = scheduleDao.deleteSchedule(schedule)

    suspend fun getSchedulesSortedByDate() = scheduleDao.getAllSchedulesSortedByDate()

    // TODO: Link with all method in DAO

}