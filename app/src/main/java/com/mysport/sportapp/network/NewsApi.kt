package com.mysport.sportapp.network

import android.content.res.Resources
import com.mysport.sportapp.R
import com.mysport.sportapp.model.NewsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("")
    fun getNews(
            @Query("country") country: String,
            @Query("category") category: String,
            @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    companion object {
        operator fun invoke(): NewsApi {
            val baseUrl: String = Resources.getSystem().getString(R.string.base_url)

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}