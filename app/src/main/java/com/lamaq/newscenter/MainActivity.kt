package com.lamaq.newscenter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.AdManagerAdViewOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter
    private var page: Int = 1
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var appOpenManager: AppOpenManager? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }

    private val btn1: Button by lazy { findViewById(R.id.btn1) }
    private val btn2: Button by lazy { findViewById(R.id.btn2) }
    private val btn3: Button by lazy { findViewById(R.id.btn3) }
    private val btn4: Button by lazy { findViewById(R.id.btn4) }
    private val btn5: Button by lazy { findViewById(R.id.btn5) }
    private val btn6: Button by lazy { findViewById(R.id.btn6) }
    private val btn7: Button by lazy { findViewById(R.id.btn7) }

    @SuppressLint("ClickableViewAccessibility", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideKeyboard(this)
        firebaseAnalytics = Firebase.analytics

        MobileAds.initialize(
            this
        ) { }
        appOpenManager = AppOpenManager(this)

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
            changeTextColor(null, searchInput)
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
            changeTextColor(null, searchInput)
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
            changeTextColor(null, searchInput)
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
            changeTextColor(btn1)
            getTopPerCatNews("general")
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn2.setOnClickListener{
            changeTextColor(btn2)
            getTopPerCatNews(btn2.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn3.setOnClickListener{
            changeTextColor(btn3)
            getTopPerCatNews(btn3.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn4.setOnClickListener{
            changeTextColor(btn4)
            getTopPerCatNews(btn4.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn5.setOnClickListener{
            changeTextColor(btn5)
            getTopPerCatNews(btn5.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn6.setOnClickListener{
            changeTextColor(btn6)
            getTopPerCatNews(btn6.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
        }

        btn7.setOnClickListener{
            changeTextColor(btn7)
            getTopPerCatNews(btn7.text.toString())
            searchInput.setText("")
            hideKeyboard(this)
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

    private fun changeTextColor(btn: Button? = null, searchbar: EditText? = null) {
        if (btn == btn1) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn2) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.white))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn3) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.white))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn4) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.white))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn5) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.white))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn6) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.white))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
        } else if (btn == btn7) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.white))
        } else if (btn == null && searchbar != null) {
            btn1.setTextColor(resources.getColor(R.color.colorAccent))
            btn2.setTextColor(resources.getColor(R.color.colorAccent))
            btn3.setTextColor(resources.getColor(R.color.colorAccent))
            btn4.setTextColor(resources.getColor(R.color.colorAccent))
            btn5.setTextColor(resources.getColor(R.color.colorAccent))
            btn6.setTextColor(resources.getColor(R.color.colorAccent))
            btn7.setTextColor(resources.getColor(R.color.colorAccent))
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

    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // FCM SDK (and your app) can post notifications.
        } else if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        }
    }
}