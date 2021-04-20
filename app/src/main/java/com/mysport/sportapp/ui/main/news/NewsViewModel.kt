package com.mysport.sportapp.ui.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.data.News

class NewsViewModel: ViewModel() {
    private val _newsList = MutableLiveData<List<News>>().apply {
        value = ArrayList<News>()
    }

    val newsList: LiveData<List<News>> = _newsList
}