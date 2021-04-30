package com.mysport.sportapp.util

import android.os.Build
import android.widget.TimePicker
import java.util.*


object TimeUtility {

    fun getTimePickerHour(tp: TimePicker): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) tp.hour else tp.currentHour

    fun getTimePickerMinute(tp: TimePicker): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) tp.minute else tp.currentMinute

    fun getDayTimestampInMillis(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    fun getDateString(timestamp: Long): String {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = timestamp

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        return "$hour:$minute:$second on $day, $month $year"
    }

    fun getDateArray(timestamp: Long): IntArray {
        val date = IntArray(7)
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = timestamp

        date[0] = calendar.get(Calendar.YEAR)
        date[1] = calendar.get(Calendar.MONTH) + 1
        date[2] = calendar.get(Calendar.DAY_OF_MONTH)

        date[3] = calendar.get(Calendar.HOUR_OF_DAY)
        date[4] = calendar.get(Calendar.MINUTE)
        date[5] = calendar.get(Calendar.SECOND)
        date[6] = calendar.get(Calendar.MILLISECOND)

        return date
    }

}