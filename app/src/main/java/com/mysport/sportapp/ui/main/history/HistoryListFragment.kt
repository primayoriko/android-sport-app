package com.mysport.sportapp.ui.main.history

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.TrainingAdapter
import com.mysport.sportapp.domain.Training
import com.mysport.sportapp.util.Converter
import com.mysport.sportapp.util.TimeUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history_list.*
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

        val orientation = resources.configuration.orientation

        val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
        val gridColumnCount: Int = 1

        recyclerViewHistory.layoutManager = GridLayoutManager(context, gridColumnCount)
        //        recyclerViewNews.setHasFixedSize(true)

        viewModel.trainingList.observe( viewLifecycleOwner,
            Observer { scheduleList ->
                    val todayTimestamp = TimeUtility.getDayTimestampInMillis(day, month, year)
                    val tomorrowTimestamp = todayTimestamp + (1000 * 60 * 60 * 24)
                    val resList =
                        scheduleList.filter { it.timestamp in todayTimestamp..tomorrowTimestamp }

                    val callback = if(isLandscape) landscapeCallback else portraitCallback

                    recyclerViewHistory?.adapter = TrainingAdapter(resList, callback)
            })

    }

    private val landscapeCallback = object: TrainingAdapter.OnClickListener {
        override fun onClick(item: Training) {
            val str = stringifyTraining(item)

            tvTrainingTypeDet.text = str[0]
            tvTrainingResultDet.text = str[1]
            tvTrainingTimeDet.text = str[2]
            tvTrainingDurationDet.text = str[3]

            view?.let {
                Glide.with(it.context).load(item.img).into(ivTrainingImageDet)
            }
        }

    }

    private val portraitCallback = object: TrainingAdapter.OnClickListener {
        override fun onClick(item: Training) {
            val converter = Converter()
            val bundle = Bundle()
            val imgByte = converter.fromBitmap(item.img)
            val str = stringifyTraining(item)

            bundle.putString("TYPE", str[0])
            bundle.putString("RESULT", str[1])
            bundle.putString("TIME", str[2])
            bundle.putString("DURATION", str[3])
            bundle.putByteArray("IMAGE", imgByte)

            findNavController()
                    .navigate(R.id.action_historyListFragment_to_historyDetailFragment, bundle)
        }

    }

    private fun stringifyTraining(training: Training): List<String> {
        val trainingType =
                if (training.type == Training.TrainingType.CYCLING) "Cycling" else "Running"
        val rawRes =
                if (training.type == Training.TrainingType.CYCLING) training.distance!!.toInt() else training.step

        val result = String.format("%d ", rawRes) + if (trainingType == "Cycling") "m" else "steps"
        val time = TimeUtility.getDateString(training.timestamp)
        val duration = String.format("%.2f s", training.duration.toFloat() / 1000F)

        val res = ArrayList<String>()

        res.add(trainingType)
        res.add(result)
        res.add(time)
        res.add(duration)

        return res
    }

}
