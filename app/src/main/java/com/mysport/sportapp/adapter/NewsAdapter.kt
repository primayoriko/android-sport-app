package com.mysport.sportapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.databinding.NewsItemBinding
import com.mysport.sportapp.data.News
import com.mysport.sportapp.ui.webnews.WebNewsActivity

class NewsAdapter (private val newsList: List<News>):
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
        RecyclerView.ViewHolder(itemView) {

        private var view: View = itemView
        private lateinit var news: News

        fun bind(news: News){
            this.news = news

            val imageUrl = StringBuilder()

            imageUrl.append(news.urlToImage)

//                    NewsItemBinding.inflate(
//                            LayoutInflater.from(view.context),
//                            view.parent as ViewGroup?,
//                            false
//                    )
            val newsItemBinding: NewsItemBinding = NewsItemBinding.bind(view)

            newsItemBinding.newsTitle.text = news.title
            newsItemBinding.newsAuthor.text = news.author
            Glide.with(view.context).load(imageUrl.toString()).into(newsItemBinding.newsImage)

            view.setOnClickListener(View.OnClickListener {
                val intent = Intent(view.context, WebNewsActivity::class.java)
                intent.putExtra("url", news.url)

                view.context.startActivity(intent)
            })

        }
    }
}

