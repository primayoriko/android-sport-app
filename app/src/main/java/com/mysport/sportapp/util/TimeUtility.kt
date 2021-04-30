package com.mysport.sportapp.util

import android.os.Build
import android.widget.TimePicker


object TimePickerUtility {

    fun getTimePickerHour(tp: TimePicker): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) tp.hour else tp.currentHour

    fun getTimePickerMinute(tp: TimePicker): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) tp.minute else tp.currentMinute

}