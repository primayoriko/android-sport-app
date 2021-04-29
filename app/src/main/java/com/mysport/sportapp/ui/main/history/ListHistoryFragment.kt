package com.mysport.sportapp.ui.main.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mysport.sportapp.R

class ListHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = ListHistoryFragment()
    }

    private lateinit var viewModel: ListHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_listhistory, container, false)
        val listView = view.findViewById<View>(R.id.recyclerViewHistory)
        val day = arguments?.getInt("Day")
        val month = arguments?.getInt("Month")
        val year = arguments?.getInt("Year")

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListHistoryViewModel::class.java)
        // TODO: Use the ViewModel

    }

}