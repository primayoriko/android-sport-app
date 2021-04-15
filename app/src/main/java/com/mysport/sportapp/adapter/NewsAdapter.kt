package com.mysport.sportapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mysport.sportapp.model.News

class NewsAdapter (val news: ArrayList<News>):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        TODO("Not yet implemented")
    }

    class NewsViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var view: View = itemView
        private var news: News? = null

        init {

        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

        fun bind(news: News){
            this.news = news

        }
    }
}

