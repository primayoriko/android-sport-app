package com.mysport.sportapp.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mysport.sportapp.R
import com.mysport.sportapp.adapter.NewsAdapter
import com.mysport.sportapp.api.NewsApi
import com.mysport.sportapp.constant.Constant.NEWS_API_API_KEY_QUERY
import com.mysport.sportapp.constant.Constant.NEWS_API_CATEGORY_QUERY
import com.mysport.sportapp.constant.Constant.NEWS_API_COUNTRY_QUERY
import com.mysport.sportapp.domain.NewsResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class NewsFragment: Fragment() {

    private val viewModel: NewsViewModel by viewModels()

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridColumnCount: Int = resources.getInteger(R.integer.grid_column_count)

        recyclerViewNews.layoutManager = GridLayoutManager(context, gridColumnCount)
        //        recyclerViewNews.setHasFixedSize(true)

        viewModel.newsList.observe( viewLifecycleOwner,
                Observer { news ->
                    recyclerViewNews?.adapter = NewsAdapter(news)
                })

        refreshLayoutNews.setOnRefreshListener {
            fetchNews()
        }

        fetchNews()
    }

    private fun fetchNews(){
        val apiKey: String = NEWS_API_API_KEY_QUERY
        val country: String = NEWS_API_COUNTRY_QUERY
        val category: String = NEWS_API_CATEGORY_QUERY

        NewsApi().getNews(country, category, apiKey).enqueue(object : Callback<NewsResponse> {

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                refreshLayoutNews?.isRefreshing = false

                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                refreshLayoutNews?.isRefreshing = false

                viewModel.newsList.value = response.body()!!.news
            }

        })
    }

}