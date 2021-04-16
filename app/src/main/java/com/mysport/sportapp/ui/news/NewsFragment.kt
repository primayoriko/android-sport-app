package com.mysport.sportapp.ui.news

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.NewsAdapter
import com.mysport.sportapp.api.NewsApiClient
import com.mysport.sportapp.api.NewsApiInterface
import com.mysport.sportapp.model.News
import com.mysport.sportapp.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.zip.Inflater

class NewsFragment: Fragment() {
    private val TAG: String = NewsFragment::class.java.canonicalName

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsList: List<News>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)

        val gridColumnCount: Int = resources.getInteger(R.integer.grid_column_count)

        val apiKey: String = resources.getString(R.string.query_api_key)
        val country: String = resources.getString(R.string.query_country)
        val category: String = resources.getString(R.string.query_category)

        val apiInterface: NewsApiInterface =
            NewsApiClient.getClient().create(NewsApiInterface::class.java)


        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(view.context, gridColumnCount);
//        recyclerView.adapter = NewsAdapter();
        val newsList: List<News> =  getNewsFromApi(apiInterface, country, category, apiKey)

        return view
    }

    private fun getNewsFromApi(apiInterface: NewsApiInterface,
              country: String, category: String, apiKey: String): List<News> {
        val call: Call<NewsResponse> = apiInterface.getNews(country, category, apiKey)

        call.enqueue(object: Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("$TAG", "Gagal fetch")
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                newsList = response!!.body()!!.news

            }

        })

        return ArrayList<News>()
    }
}