package com.mysport.sportapp.adapter

import android.R
import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import java.lang.String


//class AlarmRecyclerViewAdapter(listener: OnToggleAlarmListener) : RecyclerView.Adapter<AlarmViewHolder>() {
//    private var alarms: List<Alarm>
//    private val listener: OnToggleAlarmListener
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
//        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
//        return AlarmViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
//        val alarm: Alarm = alarms[position]
//        holder.bind(alarm, listener)
//    }
//
//    override fun getItemCount(): Int {
//        return alarms.size
//    }
//
//    override fun onViewRecycled(holder: AlarmViewHolder) {
//        super.onViewRecycled(holder)
//        holder.alarmStarted.setOnCheckedChangeListener(null)
//    }
//
//    fun setAlarms(alarms: List<Alarm>) {
//        this.alarms = alarms
//        notifyDataSetChanged()
//    }
//
//    init {
//        alarms = ArrayList<Alarm>()
//        this.listener = listener
//    }
//}
//
//class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    private val alarmTime: TextView
//    private val alarmRecurring: ImageView
//    private val alarmRecurringDays: TextView
//    private val alarmTitle: TextView
//    var alarmStarted: Switch
//
//    fun bind(alarm: Alarm, listener: OnToggleAlarmListener) {
//        val alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute())
//        alarmTime.text = alarmText
//        alarmStarted.isChecked = alarm.isStarted()
//        if (alarm.isRecurring()) {
//            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp)
//            alarmRecurringDays.setText(alarm.getRecurringDaysText())
//        } else {
//            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp)
//            alarmRecurringDays.text = "Once Off"
//        }
//        if (alarm.getTitle().length() !== 0) {
//            alarmTitle.setText(alarm.getTitle())
//        } else {
//            alarmTitle.text = "My alarm"
//        }
//        alarmStarted.setOnCheckedChangeListener { buttonView, isChecked -> listener.onToggle(alarm) }
//    }
//
//    init {
//        alarmTime = itemView.findViewById(R.id.item_alarm_time)
//        alarmStarted = itemView.findViewById(R.id.item_alarm_started)
//        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring)
//        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays)
//        alarmTitle = itemView.findViewById(R.id.item_alarm_title)
//    }
//}
//
//interface OnToggleAlarmListener {
//    fun onToggle(alarm: Alarm?)
//}
//
//class AlarmsListViewModel(application: Application) : AndroidViewModel(application) {
//    private val alarmRepository: AlarmRepository
//    private val alarmsLiveData: LiveData<List<Alarm>>
//    fun update(alarm: Alarm?) {
//        alarmRepository.update(alarm)
//    }
//
//    fun getAlarmsLiveData(): LiveData<List<Alarm>> {
//        return alarmsLiveData
//    }
//
//    init {
//        alarmRepository = AlarmRepository(application)
//        alarmsLiveData = alarmRepository.getAlarmsLiveData()
//    }
//}