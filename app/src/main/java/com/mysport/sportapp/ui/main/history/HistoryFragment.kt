package com.mysport.sportapp.ui.main.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mysport.sportapp.R
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFragment : Fragment(){


    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        val calendView = view.findViewById<View>(R.id.calendarView)
        val btn: Button = view.findViewById(R.id.buttonHistory)
        val date = Bundle()
//        calendarView.setOnDateChangeListener{ calendView, i, i2, i3 ->
//            date.putInt("Day", i)
//            date.putInt("Month", i2)
//            date.putInt("Year", i3)
//        }
        btn.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.list_history)
//            var fragment: Fragment? = null
////            when (R.id.refreshLayoutHistory) {
////                R.id.refreshLayoutHistory -> {
//                    fragment = ListHistoryFragment()
////                }
////            }
//
//
//            if (fragment != null) {
//                val transaction = activity?.supportFragmentManager?.beginTransaction()
//                transaction?.replace(R.id.refreshLayoutHistory, fragment)
//                transaction?.commit()
//            }
//            if (transaction != null) {
//                transaction.disallowAddToBackStack()
//            }
//            if (transaction != null) {
//                transaction.commit()
//            }
        }

//        val date = Bundle()
//        calendarView.setOnDateChangeListener{ calendView, i, i2, i3 ->
//            date.putInt("Day", i)
//            date.putInt("Month", i2)
//            date.putInt("Year", i3)
////            view.findNavController().navigate(R.id.recyclerViewHistory, date)
////            this.setArguments(date);
//        }

        return view
//        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.buttonHistory -> {
//
//            }
//
//            else -> {
//            }
//        }
//    }

}