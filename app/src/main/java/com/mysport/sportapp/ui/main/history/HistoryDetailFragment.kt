package com.mysport.sportapp.ui.main.history

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.util.Converter
import kotlinx.android.synthetic.main.fragment_history_detail.*

class HistoryDetailFragment : Fragment() {

    private lateinit var img: Bitmap
    private lateinit var type: String
    private lateinit var result: String
    private lateinit var time: String
    private lateinit var duration: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val converter = Converter()
        arguments?.let {
            img = converter.toBitmap(it.getByteArray("IMAGE")!!)
            type = it.getString("TYPE")!!
            result = it.getString("RESULT")!!
            time = it.getString("TIME")!!
            duration = it.getString("DURATION")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTrainingType.text = type
        tvTrainingDuration.text = duration
        tvTrainingResult.text = result
        tvTrainingTime.text = time

        Glide.with(view.context).load(img).into(ivTrainingImage)

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HistoryDetailFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HistoryDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}