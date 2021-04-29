package com.mysport.sportapp.ui.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.data.News

class NewsViewModel: ViewModel() {

    val newsList: MutableLiveData<List<News>> by lazy {
        MutableLiveData<List<News>>()
    }

}