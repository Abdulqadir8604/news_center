package com.lamaq.newscenter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper


class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    private var page: Int = 1
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }

    private val btn1: Button by lazy { findViewById(R.id.btn1) }
    private val btn2: Button by lazy { findViewById(R.id.btn2) }
    private val btn3: Button by lazy { findViewById(R.id.btn3) }
    private val btn4: Button by lazy { findViewById(R.id.btn4) }
    private val btn5: Button by lazy { findViewById(R.id.btn5) }
    private val btn6: Button by lazy { findViewById(R.id.btn6) }
    private val btn7: Button by lazy { findViewById(R.id.btn7) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        window.navigationBarColor = resources.getColor(R.color.colorPrimary)

        val searchInput: EditText = findViewById(R.id.search_input)
        searchInput.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        searchInput.isEnabled = true
        searchInput.setOnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP)
                if (event.x >= searchInput.right - searchInput.compoundDrawables[drawableRight].bounds.width()
                ) {
                    hideKeyboard(this)
                    searchInput.clearFocus()
                    if (searchInput.text.toString().isNotEmpty()) {
                        getTopSearchNews(searchInput.text.toString())
                    }
                    else
                        Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
                }
            false
        }
        searchInput.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(this)
                if (searchInput.text.toString().isNotEmpty()) {
                    getTopSearchNews(searchInput.text.toString())
                }
                else
                    Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(this)
                if (searchInput.text.toString().isNotEmpty()) {
                    getTopSearchNews(searchInput.text.toString())
                }
                else
                    Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }

        btn1.setOnClickListener{
            getTopPerCatNews("general")
            searchInput.setText("")
        }

        btn2.setOnClickListener{
            getTopPerCatNews(btn2.text.toString())
            searchInput.setText("")
        }

        btn3.setOnClickListener{
            getTopPerCatNews(btn3.text.toString())
            searchInput.setText("")
        }

        btn4.setOnClickListener{
            getTopPerCatNews(btn4.text.toString())
            searchInput.setText("")
        }

        btn5.setOnClickListener{
            getTopPerCatNews(btn5.text.toString())
            searchInput.setText("")
        }

        btn6.setOnClickListener{
            getTopPerCatNews(btn6.text.toString())
            searchInput.setText("")
        }

        btn7.setOnClickListener{
            getTopPerCatNews(btn7.text.toString())
            searchInput.setText("")
        }

        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.hasFixedSize()

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
        val news = TopSearchNewsApiService.newsInstance.getTopSearchNews(search, "en", "publishedAt", page)
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

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}