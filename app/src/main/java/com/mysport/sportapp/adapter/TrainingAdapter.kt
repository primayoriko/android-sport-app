package com.mysport.sportapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.databinding.FragmentHistoryListBinding
import com.mysport.sportapp.databinding.ItemTrainingBinding
import com.mysport.sportapp.ui.main.history.HistoryListFragment
import com.mysport.sportapp.util.Converter
import com.mysport.sportapp.util.TimeUtility
import kotlinx.android.synthetic.main.fragment_history_list.*
import timber.log.Timber
import java.util.*

class TrainingAdapter(
    private var trainingList: List<Training>,
    private val isPortrait: Boolean,
    private val fragment: Fragment
    ): RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    override fun getItemCount(): Int = trainingList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_training, parent, false)

        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainingList[position], isPortrait, fragment)
    }

    override fun onViewRecycled(holder: TrainingViewHolder) {
        super.onViewRecycled(holder)
    }

    class TrainingViewHolder(private val view: View):
        RecyclerView.ViewHolder(view) {

        private lateinit var training: Training

//        fun bind(training: Training, listener: OnToggleTrainingListener) {
        fun bind(training: Training, isPortrait: Boolean, fragment: Fragment) {
            this.training = training

            val binding = ItemTrainingBinding.bind(view)
            val trainingType =
                if (training.type == TrainingType.CYCLING) "Cycling" else "Running"
            val result =
                if (training.type == TrainingType.CYCLING) training.distance else training.step

            val resultStr = "$result " + if (trainingType == "Cycling") "m" else "steps"
            val time = TimeUtility.getDateString(training.timestamp)
            val duration = String.format("%.2f s", training.duration.toFloat() / 1000F)

            binding.tvTrainingType.text = trainingType
            binding.tvTrainingDuration.text = duration
            binding.tvTrainingResult.text = resultStr
            binding.tvTrainingTime.text = time

            Glide.with(view.context).load(training.img).into(binding.ivTrainingImage)

            if(isPortrait) {
                val converter = Converter()
                val bundle = Bundle()
                val imgByte = converter.fromBitmap(training.img)

                bundle.putString("TYPE", trainingType)
                bundle.putString("RESULT", resultStr)
                bundle.putString("TIME", time)
                bundle.putString("DURATION", duration)
                bundle.putByteArray("IMAGE", imgByte)

                view.setOnClickListener(
                    View.OnClickListener {
                        view.findFragment<HistoryListFragment>()
                            .findNavController()
                            .navigate(R.id.action_historyListFragment_to_historyDetailFragment, bundle)
                    })
            } else {
                view.setOnClickListener(
                    View.OnClickListener {
                        fragment.tvTrainingType.text = trainingType
                        fragment.tvTrainingDuration.text = duration
                        fragment.tvTrainingResult.text = resultStr
                        fragment.tvTrainingTime.text = time

                        Glide.with(view.context).load(training.img).into(fragment.ivTrainingImage)
                    })
            }
//            scheduleStarted.setOnCheckedChangeListener
//                  { buttonView, isChecked -> listener.onToggle(schedule) }
        }

    }

    interface OnClickedListener {
        fun onClick(training: Training?)
    }
}