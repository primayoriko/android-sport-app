package com.mysport.sportapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.databinding.ItemTrainingBinding
import com.mysport.sportapp.util.TimeUtility
import timber.log.Timber
import java.util.*

class TrainingAdapter(private var trainingList: List<Training>):
    RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    override fun getItemCount(): Int = trainingList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_training, parent, false)

        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainingList[position])
    }

    override fun onViewRecycled(holder: TrainingViewHolder) {
        super.onViewRecycled(holder)
    }

    class TrainingViewHolder(private val view: View):
        RecyclerView.ViewHolder(view) {

        private lateinit var training: Training

//        fun bind(training: Training, listener: OnToggleTrainingListener) {
        fun bind(training: Training) {
            this.training = training

            val binding = ItemTrainingBinding.bind(view)
            val trainingType =
                if (training.type == TrainingType.CYCLING) "Cycling" else "Running"
            val result =
                if (training.type == TrainingType.CYCLING) training.distance else training.step
            val resultStr = "$result " + if (trainingType == "Cycling") "m" else "steps"

            val time = TimeUtility.getDateString(training.timestamp)
            val duration = String.format("%.2f", training.duration.toFloat() / 1000F)

            binding.trainingType.text = trainingType
            binding.trainingDuration.text = duration
            binding.trainingResult.text = resultStr
            binding.trainingTime.text = time

            Glide.with(view.context).load(training.img).into(binding.trainingImage)
//            view.setOnClickListener(View.OnClickListener {
//                val intent = Intent(view.context, NewsActivity::class.java)
//                intent.putExtra("url", news.url)
//
//                view.context.startActivity(intent)
//            })
//
//            scheduleStarted.setOnCheckedChangeListener
//                  { buttonView, isChecked -> listener.onToggle(schedule) }
        }

//            view.setOnClickListener(View.OnClickListener {
//                val intent = Intent(view.context, NewsActivity::class.java)
//                intent.putExtra("url", news.url)
//
//                view.context.startActivity(intent)
//            })
//
//            scheduleStarted.setOnCheckedChangeListener
//                  { buttonView, isChecked -> listener.onToggle(schedule) }
    }

    interface OnClickedListener {
        fun onClick(training: Training?)
    }
}