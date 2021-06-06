package com.mysport.sportapp.domain

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    @SerializedName("articles") val news: List<News>
)