package com.mysport.sportapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mysport.sportapp.R
import com.mysport.sportapp.databinding.NewsItemBinding
import com.mysport.sportapp.model.News
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

//        init {
//            itemView.setOnClickListener(this)
//        }
//        override fun onClick(v: View?) {
//            val intent = Intent(context, WebNewsActivity::class.java)
//            intent.putExtra("url", news.url)
//            context.startActivity(intent)
//        }

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

            view.setOnClickListener(View.OnClickListener {
                val intent = Intent(view.context, WebNewsActivity::class.java)
                intent.putExtra("url", news.url)

                view.context.startActivity(intent)
            })

        }
    }
}

