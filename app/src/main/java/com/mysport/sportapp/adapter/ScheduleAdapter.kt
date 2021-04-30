package com.mysport.sportapp.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Schedule
import com.mysport.sportapp.data.Schedule.ScheduleType
import com.mysport.sportapp.data.Training.TrainingType
import com.mysport.sportapp.databinding.ItemScheduleBinding


//class ScheduleAdapter(listener: OnToggleScheduleListener):
class ScheduleAdapter(private var scheduleList: List<Schedule>):
        RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
//    private val listener: OnToggleScheduleListener
    override fun getItemCount(): Int = scheduleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)

        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun onViewRecycled(holder: ScheduleViewHolder) {
        super.onViewRecycled(holder)
//        holder.scheduleStarted.setOnCheckedChangeListener(null)
    }

    fun setSchedules(scheduleList: List<Schedule>) {
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }

    class ScheduleViewHolder(private val view: View):
            RecyclerView.ViewHolder(view) {

        private lateinit var schedule: Schedule

//        fun bind(schedule: Schedule, listener: OnToggleScheduleListener) {
        fun bind(schedule: Schedule) {
            this.schedule = schedule

            val title = schedule.title
            val time = "${schedule.hour}:${schedule.minute}"
            val duration = "${schedule.durationInMinutes}"
            val trainingType = if(schedule.trainingType == TrainingType.CYCLING) "C" else "R"
            val target = "${schedule.target} " + if (trainingType == "C") "m" else "steps"
            val isAutomated = if(schedule.isAutomated) "YES" else "NO"
            val status = if(schedule.isActive) "V" else "X"
            val scheduleType =
                    if(schedule.scheduleType == ScheduleType.EXACT) "Exact" else "Routine"

            val binding: ItemScheduleBinding = ItemScheduleBinding.bind(view)

            binding.tvScheduleTitle.text = title
            binding.tvScheduleTime.text = time
            binding.tvScheduleType.text = scheduleType
            binding.tvTrainingType.text = trainingType
            binding.tvTrainingTarget.text = target
            binding.tvScheduleDuration.text = duration
            binding.tvAutomated.text = isAutomated
            binding.tvScheduleDayDate.text = "hehehe" // TODO: set day date string
            binding.tvScheduleStatus.text = status
            binding.tvScheduleStatus
                    .setBackgroundColor(if (schedule.isActive) Color.GREEN else Color.RED)

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

    }

//    interface OnToggleScheduleListener {
//        fun onToggle(schedule: Schedule?)
//    }

}


