package com.mysport.sportapp.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.ScheduleAdapter
import com.mysport.sportapp.adapter.TrainingAdapter
import com.mysport.sportapp.util.TimeUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history_list.*
import kotlinx.android.synthetic.main.fragment_scheduler.*
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class HistoryListFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    private var day by Delegates.notNull<Int>()
    private var month by Delegates.notNull<Int>()
    private var year by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            day = it.getInt("DAY")
            month = it.getInt("MONTH")
            year = it.getInt("YEAR")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridColumnCount: Int = 1

        recyclerViewHistory.layoutManager = GridLayoutManager(context, gridColumnCount)
        //        recyclerViewNews.setHasFixedSize(true)

        viewModel.trainingList.observe( viewLifecycleOwner,
            Observer { scheduleList ->
                val todayTimestamp = TimeUtility.getDayTimestampInMillis(day, month, year)
                val tomorrowTimestamp = todayTimestamp + (1000 * 60 * 60 * 24)
//                Timber.d("hehehe\nhehehe\n")
//                Timber.d(todayTimestamp.toString())
//                Timber.d(tomorrowTimestamp.toString())
                val resList =
                    scheduleList.filter { it.timestamp in todayTimestamp..tomorrowTimestamp }

                recyclerViewHistory?.adapter = TrainingAdapter(resList)
            })

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HistoryListFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                HistoryListFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}