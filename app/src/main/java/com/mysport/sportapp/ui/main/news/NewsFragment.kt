package com.mysport.sportapp.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.NewsAdapter
import com.mysport.sportapp.network.NewsApi
import com.mysport.sportapp.model.News
import com.mysport.sportapp.model.NewsResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class NewsFragment: Fragment() {
//    private val TAG: String = NewsFragment::class.java.canonicalName

    private lateinit var newsViewModel: NewsViewModel

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        refreshLayoutNews.setOnRefreshListener {
            fetchNews()

        }

        fetchNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        return inflater.inflate(R.layout.fragment_news, container, false)

    }

    private fun fetchNews(){
        val apiKey: String = resources.getString(R.string.query_api_key)
        val country: String = resources.getString(R.string.query_country)
        val category: String = resources.getString(R.string.query_category)

        NewsApi().getNews(country, category, apiKey).enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                refreshLayoutNews.isRefreshing = false
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                refreshLayoutNews.isRefreshing = false

                val newsList: List<News> = response.body()!!.news

                showNews(newsList)

            }

        })
    }

    private fun showNews(news: List<News>) {
        val gridColumnCount: Int = resources.getInteger(R.integer.grid_column_count)

        recyclerViewNews.layoutManager = GridLayoutManager(this.context, gridColumnCount)
        recyclerViewNews.adapter = NewsAdapter(news)
//        recyclerViewNews.setHasFixedSize(true)
    }
}