package com.lamaq.newscenter

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    var page: Int = 1
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    private val btn1: Button by lazy { findViewById<Button>(R.id.btn1) }
    private val btn2: Button by lazy { findViewById<Button>(R.id.btn2) }
    private val btn3: Button by lazy { findViewById<Button>(R.id.btn3) }
    private val btn4: Button by lazy { findViewById<Button>(R.id.btn4) }
    private val btn5: Button by lazy { findViewById<Button>(R.id.btn5) }
    private val btn6: Button by lazy { findViewById<Button>(R.id.btn6) }
    private val btn7: Button by lazy { findViewById<Button>(R.id.btn7) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val textInputLayout:TextInputLayout = findViewById(R.id.textField)
//        val inputText = textInputLayout.editText
//
//        textInputLayout.setEndIconOnClickListener {
//            if (inputText != null) {
//                getTopSearchNews(inputText.text.toString())
//            }
//        }

        btn1.setOnClickListener{
            getTopPerCatNews(btn1.text.toString())
        }

        btn2.setOnClickListener{
            getTopPerCatNews(btn2.text.toString())
        }

        btn3.setOnClickListener{
            getTopPerCatNews(btn3.text.toString())
        }

        btn4.setOnClickListener{
            getTopPerCatNews(btn4.text.toString())
        }

        btn5.setOnClickListener{
            getTopPerCatNews(btn5.text.toString())
        }

        btn6.setOnClickListener{
            getTopPerCatNews(btn6.text.toString())
        }

        btn7.setOnClickListener{
            getTopPerCatNews(btn7.text.toString())
        }

        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = mLayoutManager

        val helper: SnapHelper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)
        helper.onFling(1, 0)

        progressBar.visibility = ProgressBar.VISIBLE
        getTopNews()
        if (recyclerView.canScrollHorizontally(1)){
            page++
        }


    }

    private fun getTopSearchNews(search: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        val news = TopSearchNewsApiService.newsInstance.getTopSearchNews("in", search, page, 100)
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
                    progressBar.visibility = ProgressBar.GONE
                    adapter = Adapter(this@MainActivity, news.articles)
                    recyclerView.adapter = adapter
                }

            }
        })
    }

    private fun getTopNews() {
        progressBar.visibility = ProgressBar.VISIBLE
        val news = TopNewsApiService.newsInstance.getTopNews("in", page, 100)
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
                    progressBar.visibility = ProgressBar.GONE
                    adapter = Adapter(this@MainActivity, news.articles)
                    recyclerView.adapter = adapter
                }

            }
        })
    }

    private fun getTopPerCatNews(category: String){
        progressBar.visibility = ProgressBar.VISIBLE
        val news = TopPerCatNewsApiService.newsInstance.getTopCatNews("in", category, page, 100)
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
                    progressBar.visibility = ProgressBar.GONE
                    adapter = Adapter(this@MainActivity, news.articles)
                    recyclerView.adapter = adapter
                }

            }
        })
    }
}