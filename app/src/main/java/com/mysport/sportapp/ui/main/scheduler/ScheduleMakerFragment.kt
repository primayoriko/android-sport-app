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
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.util.TimePickerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_schedule_maker.*
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class ScheduleMakerFragment : Fragment() {

    private val viewModel: SchedulerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_maker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnScheduleMaker.setOnClickListener {
            createSchedule()
        }

        rgScheduleType.setOnCheckedChangeListener {
            _, selectedID ->
                if (selectedID == 0){
                    layoutDays.visibility = View.GONE
                    layoutDate.visibility = View.VISIBLE
                } else {
                    layoutDate.visibility = View.GONE
                    layoutDays.visibility = View.VISIBLE
                }
        }

//        btnSchedulerSwitchToDay.setOnClickListener{
//            findNavController()
//                    .navigate(R.id.action_exactScheduleMakerFragment_to_dayScheduleMakerFragment)
//        }
    }

    private fun createSchedule() {
        val scheduleType =
                if (rgScheduleType.checkedRadioButtonId == 1) ScheduleType.ROUTINE
                else ScheduleType.EXACT
        val hour = TimePickerUtility.getTimePickerHour(tpScheduleTime)
        val minute = TimePickerUtility.getTimePickerMinute(tpScheduleTime)
        val duration = etScheduleDuration.text.toString().toIntOrNull()
        val target = etScheduleTarget.text.toString().toIntOrNull()
        val trainingType =
                if (rgTrainingType.checkedRadioButtonId == 1) TrainingType.RUNNING
                else TrainingType.CYCLING

//        val isAutomated = rgAutomate.checkedRadioButtonId == 0
        val schedule: Schedule
        val toastText: String

        if(scheduleType == ScheduleType.ROUTINE) {
            schedule = Schedule(
                    etScheduleTitle.text.toString(),
                    trainingType,
                    scheduleType,
                    hour,
                    minute,
                    duration!!,
                    target!!,
                    rgAutomate.checkedRadioButtonId == 0,
                    true,
                    Random.nextInt(1, 100000),
                    0,
                    0,
                    0,
                    cbMonday.isChecked,
                    cbTuesday.isChecked,
                    cbWednesday.isChecked,
                    cbThursday.isChecked,
                    cbFriday.isChecked,
                    cbSaturday.isChecked,
                    cbSunday.isChecked,
            )

            toastText = "Scheduled successfully at $hour:$minute every selected day(s)"

        } else {
            val day = tpScheduleDate.dayOfMonth
            val month = tpScheduleDate.month
            val year = tpScheduleDate.year

            schedule = Schedule(
                    etScheduleTitle.text.toString(),
                    trainingType,
                    scheduleType,
                    hour,
                    minute,
                    duration!!,
                    target!!,
                    rgAutomate.checkedRadioButtonId == 0,
                    true,
                    Random.nextInt(1, 100000),
                    day,
                    month,
                    year,
                    isMonday = false,
                    isTuesday = false,
                    isWednesday = false,
                    isThursday = false,
                    isFriday = false,
                    isSaturday = false,
                    isSunday = false,
            )

            toastText = "Scheduled successfully at $hour:$minute in $day $month $year"
        }

        val toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG)

        Timber.d(schedule.toString())

        viewModel.insert(schedule)

        schedule.invoke(requireContext())

        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()

        findNavController().navigate(R.id.action_scheduleMakerFragment_to_navigation_scheduler)
    }

//        val spinner: Spinner? =  view.findViewById(R.id.dropdownTracker)
//
//        val values = resources.getStringArray(R.array.track_type)
//        val adapter = ArrayAdapter(
//            this.requireActivity(),
//            android.R.layout.simple_spinner_item,
//            values
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//
//        spinner?.adapter = adapter
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
////                Log.v("item", parent.getItemAtPosition(position) as String)
//                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }

}