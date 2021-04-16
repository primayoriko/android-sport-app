package com.mysport.sportapp.ui.news

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.NewsAdapter
import java.util.zip.Inflater

class NewsFragment: Fragment() {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val gridColumnCount: Int = resources.getInteger(R.integer.grid_column_count)

        val view: View = inflater.inflate(R.layout.fragment_news, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(view.context, gridColumnCount);
//        recyclerView.adapter = NewsAdapter();

        return view
    }
}