package com.mysport.sportapp.ui.main.scheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Training.TrainingType
import kotlinx.android.synthetic.main.fragment_exact_schedule_maker.*

class ExactScheduleMakerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exact_schedule_maker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnExactScheduleMaker.setOnClickListener {
            createSchedule()

        }

        btnSchedulerSwitchToDay.setOnClickListener{
//            findNavController().navigate()
        }
    }

    private fun createSchedule() {
        val trainTypeId = rgScheduleType.checkedRadioButtonId
        val trainType =
                if (trainTypeId == 1) TrainingType.RUNNING
                else TrainingType.CYCLING

    }

}