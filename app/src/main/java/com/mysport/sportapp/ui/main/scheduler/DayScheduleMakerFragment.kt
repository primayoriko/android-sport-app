package com.mysport.sportapp.ui.main.scheduler

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Schedule.ScheduleType
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.util.TimePickerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_day_schedule_maker.*
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class DayScheduleMakerFragment : Fragment() {

    private val viewModel: SchedulerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_schedule_maker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnScheduleMaker.setOnClickListener {
            createSchedule()
        }

        btnSchedulerSwitchToExact.setOnClickListener{
//            findNavController()
//                    .navigate(R.id.action_dayScheduleMakerFragment_to_exactScheduleMakerFragment)
        }
    }

    private fun createSchedule() {
//        val hour = TimePickerUtility.getTimePickerHour(tpScheduleTime)
//        val minute = TimePickerUtility.getTimePickerMinute(tpScheduleTime)
//        val isRecurring = rgRecurring.checkedRadioButtonId == 0
//        val trainingType =
//                if (rgScheduleType.checkedRadioButtonId == 1) TrainingType.RUNNING
//                else TrainingType.CYCLING
////        val duration = etScheduleDuration.text.toString().toInt()
////        val target = etScheduleTarget.text.toString().toInt()
////        val isAutomated = rgAutomate.checkedRadioButtonId == 0
//
//        val recurringText = if (isRecurring) "recurring" else "one-time"
//        val toastText = "Scheduled successfully $recurringText at $hour:$minute"
//        val toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG)
//
//        val schedule = Schedule(
//                etScheduleTitle.text.toString(),
//                trainingType,
//                ScheduleType.DAY,
//                hour,
//                minute,
//                etScheduleDuration.text.toString().toInt(),
//                etScheduleTarget.text.toString().toInt(),
//                isRecurring,
//                rgAutomate.checkedRadioButtonId == 0,
//                true,
//                Random.nextInt(1, 100000),
//                0,
//                0,
//                0,
//                cbMonday.isChecked,
//                cbTuesday.isChecked,
//                cbWednesday.isChecked,
//                cbThursday.isChecked,
//                cbFriday.isChecked,
//                cbSaturday.isChecked,
//                cbSunday.isChecked,
//        )
//
//        Timber.d(schedule.toString())
//
//        viewModel.insert(schedule)
//
//        schedule.invoke(requireContext())
//
//        toast.setGravity(Gravity.CENTER, 0, 0)
//        toast.show()
//
//        findNavController().navigate(R.id.action_dayScheduleMakerFragment_to_navigation_scheduler)
    }

}