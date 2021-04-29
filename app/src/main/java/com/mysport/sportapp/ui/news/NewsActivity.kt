package com.mysport.sportapp.ui.news

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mysport.sportapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.content_news.*

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        setSupportActionBar(newsToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val url: String? = intent.getStringExtra("url")

        webViewNews.loadUrl(url!!)
        webViewNews.webViewClient = WebViewClient()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}