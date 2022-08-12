package com.lamaq.newscenter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.PackageManagerCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.*


/** Prefetches App Open Ads.  */
class AppOpenManager(mainActivity: MainActivity) : Application.ActivityLifecycleCallbacks {
    private var appOpenAd: AppOpenAd? = null
    private var isShowingAd = false
    private val currentActivity: Activity? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var mainActivity: MainActivity? = null
    private var loadTime: Long = 0

    /** Request an ad  */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }

        loadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                loadTime = Date().time
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
            }
        }
        val request: AdRequest = AdRequest.Builder().build()
        mainActivity?.let {
            AppOpenAd.load(
                it, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback as AppOpenAdLoadCallback
            )
        }

    }

    /** Utility method to check if ad was loaded more than n hours ago.  */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Creates and returns ad request.  */
    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4) && !isShowingAd


    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    }

    /** Constructor  */
    init {
        this.mainActivity = mainActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.mainActivity!!.registerActivityLifecycleCallbacks(this)
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //TODO("Not yet implemented")
    }

    override fun onActivityStarted(p0: Activity) {
        showAdIfAvailable()
    }

    override fun onActivityResumed(p0: Activity) {
        //TODO("Not yet implemented")
    }

    override fun onActivityPaused(p0: Activity) {
        //TODO("Not yet implemented")
    }

    override fun onActivityStopped(p0: Activity) {
        //TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        //TODO("Not yet implemented")
    }

    @SuppressLint("RestrictedApi")
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            Log.d(PackageManagerCompat.LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }

            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            if (currentActivity != null) {
                appOpenAd!!.show(currentActivity)
            }
        } else {
            Log.d(PackageManagerCompat.LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

}