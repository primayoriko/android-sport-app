package com.mysport.sportapp.api

import com.mysport.sportapp.data.NewsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines/")
    fun getNews(
            @Query("country") country: String,
            @Query("category") category: String,
            @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    companion object {
        operator fun invoke(): NewsApi {
            val baseUrl: String = "https://newsapi.org/"

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging)

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(NewsApi::class.java)
        }
    }
}