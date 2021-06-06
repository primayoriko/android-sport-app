package com.mysport.sportapp.ui.main.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.domain.News

class NewsViewModel: ViewModel() {

    val newsList: MutableLiveData<List<News>> by lazy {
        MutableLiveData<List<News>>()
    }

}