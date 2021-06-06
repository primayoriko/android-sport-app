package com.mysport.sportapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.domain.Training
import com.mysport.sportapp.domain.Training.TrainingType
import com.mysport.sportapp.databinding.ItemTrainingBinding
import com.mysport.sportapp.util.TimeUtility

class TrainingAdapter(
        private var trainingList: List<Training>,
        private val callback: OnClickListener
): RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    interface OnClickListener {
        fun onClick(item: Training)
    }

    override fun getItemCount(): Int = trainingList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainingList[position])
        holder.view.setOnClickListener { callback.onClick(trainingList[position]) }
    }

    class TrainingViewHolder(val view: View):
        RecyclerView.ViewHolder(view) {

        private lateinit var training: Training

        fun bind(training: Training) {
            this.training = training
            val binding = ItemTrainingBinding.bind(view)
            val trainingType =
                if (training.type == TrainingType.CYCLING) "Cycling" else "Running"
            val rawRes =
                if (training.type == TrainingType.CYCLING) training.distance!!.toInt() else training.step

            val result = String.format("%d ", rawRes) + if (trainingType == "Cycling") "m" else "steps"
            val time = TimeUtility.getDateString(training.timestamp)
            val duration = String.format("%.2f s", training.duration.toFloat() / 1000F)

            binding.tvTrainingType.text = trainingType
            binding.tvTrainingDuration.text = duration
            binding.tvTrainingResult.text = result
            binding.tvTrainingTime.text = time

            Glide.with(view.context).load(training.img).into(binding.ivTrainingImage)
        }

    }

}