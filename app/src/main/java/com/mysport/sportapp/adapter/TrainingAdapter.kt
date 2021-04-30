package com.mysport.sportapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Training

class TrainingAdapter(private var trainingList: List<Training>):
    RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    override fun getItemCount(): Int = trainingList.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainingList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)

        return TrainingViewHolder(view)
    }

    override fun onViewRecycled(holder: TrainingViewHolder) {
        super.onViewRecycled(holder)
    }

    class TrainingViewHolder(private val view: View):
        RecyclerView.ViewHolder(view) {

        private lateinit var training: Training

//        fun bind(training: Training, listener: OnToggleTrainingListener) {
        fun bind(training: Training) {}

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