package com.mysport.sportapp.ui.main.scheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.ScheduleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scheduler.*
import timber.log.Timber

@AndroidEntryPoint
class SchedulerFragment : Fragment() {
//    private var param1: String? = null
//    private var param2: String? = null
    private val viewModel: SchedulerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scheduler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridColumnCount: Int = 1

        recyclerViewScheduler.layoutManager = GridLayoutManager(context, gridColumnCount)
        //        recyclerViewNews.setHasFixedSize(true)

        viewModel.scheduleList.observe( viewLifecycleOwner,
                Observer { scheduleList ->
                    recyclerViewScheduler?.adapter = ScheduleAdapter(scheduleList)
                })

        fabScheduler.setOnClickListener {
             findNavController()
                     .navigate(R.id.action_navigation_scheduler_to_exactScheduleMakerFragment)
        }

        refreshLayoutScheduler.setOnRefreshListener {
            recyclerViewScheduler?.adapter?.notifyDataSetChanged()
        }
    }

}