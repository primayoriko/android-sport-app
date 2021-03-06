package com.mysport.sportapp.ui.main.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history.*

@AndroidEntryPoint
class HistoryFragment : Fragment(){

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnHistory.setOnClickListener{
            val day = dpDate.dayOfMonth
            val month = dpDate.month
            val year = dpDate.year

            val bundle = Bundle()
            bundle.putInt("DAY", day)
            bundle.putInt("MONTH", month)
            bundle.putInt("YEAR", year)

            findNavController()
                .navigate(R.id.action_navigation_history_to_historyListFragment, bundle)
        }
    }

}