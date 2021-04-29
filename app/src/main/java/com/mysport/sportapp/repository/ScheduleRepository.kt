package com.mysport.sportapp.repository

import androidx.lifecycle.LiveData
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.db.dao.ScheduleDao
import javax.inject.Inject


class ScheduleRepository @Inject constructor(
        val scheduleDao: ScheduleDao
) {
    suspend fun insert(schedule: Schedule) = scheduleDao.insert(schedule)

    suspend fun delete(schedule: Schedule) = scheduleDao.delete(schedule)

    suspend fun update(schedule: Schedule) = scheduleDao.update(schedule)

    fun getAllSortedByDate() = scheduleDao.getAllSortedByDate()

    // TODO: Link with all method in DAO

}