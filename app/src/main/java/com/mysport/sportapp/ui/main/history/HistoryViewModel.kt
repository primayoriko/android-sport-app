package com.mysport.sportapp.ui.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mysport.sportapp.data.Training

class HistoryViewModel : ViewModel() {

    val historyList: MutableLiveData<List<Training>> by lazy {
        MutableLiveData<List<Training>>()
    }

}