package com.mysport.sportapp.ui.main.scheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Schedule.ScheduleType
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.util.TimePickerUtility
import kotlinx.android.synthetic.main.fragment_day_schedule_maker.*
import timber.log.Timber

class DayScheduleMakerFragment : Fragment() {

    private val viewModel: SchedulerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_schedule_maker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnExactScheduleMaker.setOnClickListener {
            createSchedule()

        }

        btnSchedulerSwitchToExact.setOnClickListener{
            findNavController()
                    .navigate(R.id.action_dayScheduleMakerFragment_to_exactScheduleMakerFragment)
        }
    }

    private fun createSchedule() {
        val trainType =
                if (rgScheduleType.checkedRadioButtonId == 1) TrainingType.RUNNING
                else TrainingType.CYCLING

        val isRecurring = rgRecurring.checkedRadioButtonId == 1

        val schedule = Schedule(
                    etScheduleTitle.text.toString(),
                    trainType,
                    ScheduleType.DAY,
                    TimePickerUtility.getTimePickerHour(tpScheduleTime),
                    TimePickerUtility.getTimePickerMinute(tpScheduleTime),
                    0,
                    0,
                    isRecurring
                )

        Timber.d(schedule.toString())

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