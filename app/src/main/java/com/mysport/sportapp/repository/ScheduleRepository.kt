package com.mysport.sportapp.repository

import com.mysport.sportapp.domain.Schedule
import com.mysport.sportapp.db.dao.ScheduleDao
import javax.inject.Inject


class ScheduleRepository @Inject constructor(
        val scheduleDao: ScheduleDao
) {
    suspend fun insert(schedule: Schedule) = scheduleDao.insert(schedule)

    suspend fun delete(schedule: Schedule) = scheduleDao.delete(schedule)

    suspend fun update(schedule: Schedule) = scheduleDao.update(schedule)

    fun get(id: Int) = scheduleDao.get(id)

    fun getAllSortedByID() = scheduleDao.getAllSortedByID()

    fun getAllSortedByStatus() = scheduleDao.getAllSortedByStatus()

    fun getAllSortedByDate() = scheduleDao.getAllSortedByDate()

    fun getAllActive() = scheduleDao.getAllActive()

}