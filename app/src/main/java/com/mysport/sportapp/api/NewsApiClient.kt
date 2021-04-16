package com.mysport.sportapp.api

import android.content.res.Resources
import com.mysport.sportapp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiClient {
    companion object {
        fun getClient(): Retrofit {
            val BASE_URL: String = Resources.getSystem().getString(R.string.base_url)

            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}