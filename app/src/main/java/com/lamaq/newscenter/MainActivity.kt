package com.lamaq.newscenter

import android.os.Bundle
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper


class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = mLayoutManager

        val helper: SnapHelper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        getNews()
    }

    private fun getNews() {
        val news = NewsApiService.newsInstance.getNews("in", 100)
        news.enqueue(object : retrofit2.Callback<NewsList> {
            override fun onFailure(call: retrofit2.Call<NewsList>, t: Throwable) {
                println("Error")
            }

            override fun onResponse(
                call: retrofit2.Call<NewsList>,
                response: retrofit2.Response<NewsList>
            ) {
                val news = response.body()
                if (news != null) {
                    adapter = Adapter(this@MainActivity, news.articles)
                    recyclerView.adapter = adapter
                }

            }
        })
    }
}