package com.mysport.sportapp.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.mysport.sportapp.R
import kotlin.properties.Delegates

class HistoryListFragment : Fragment() {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Snackbar.make(
//            requireActivity().findViewById(R.id.activity_main),
//            "$day $month $year",
//            Snackbar.LENGTH_LONG
//        ).show()
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HistoryListFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                HistoryListFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}