package com.mysport.sportapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.databinding.NewsItemBinding
import com.mysport.sportapp.model.News

class NewsAdapter (val newsList: ArrayList<News>):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun getItemCount(): Int = newsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    class NewsViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var view: View = itemView
        private var news: News? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

        fun bind(news: News){
            this.news = news

            val imageUrl = StringBuilder()

            imageUrl.append(news.urlToImage)

            val newsItemBinding: NewsItemBinding = NewsItemBinding.bind(view)
//                    NewsItemBinding.inflate(
//                            LayoutInflater.from(view.context),
//                            view.parent as ViewGroup?,
//                            false
//                    )
            newsItemBinding.newsTitle.text = news.title
            newsItemBinding.newsAuthor.text = news.author
            Glide.with(view.context).load(imageUrl.toString()).into(newsItemBinding.newsImage)

        }
    }
}

