package com.mysport.sportapp.ui.main.scheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Training.TrainingType
import kotlinx.android.synthetic.main.fragment_day_schedule_maker.*

class DayScheduleMakerFragment : Fragment() {

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
        val trainTypeId = rgScheduleType.checkedRadioButtonId
        val trainType =
                if (trainTypeId == 1) TrainingType.RUNNING
                else TrainingType.CYCLING

    }

}