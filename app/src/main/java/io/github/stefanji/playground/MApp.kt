package io.github.stefanji.playground

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary


/**
 * Create by jy on 2019-05-31
 */
private const val TAG = "MApp"

class MApp : MultiDexApplication() {
    private val lifecycleCallbacks = ActivityLife()

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...

        registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}


class ActivityLife : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {
        Log.d(TAG, "${activity.simpleName()} onPaused")
    }

    override fun onActivityResumed(activity: Activity?) {
        Log.d(TAG, "${activity.simpleName()} onResumed")
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d(TAG, "${activity.simpleName()} onStarted")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d(TAG, "${activity.simpleName()} onDestroyed")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.d(TAG, "${activity.simpleName()} onSaveInstance")
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d(TAG, "${activity.simpleName()} onStopped")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d(TAG, "${activity.simpleName()} onCreated")
    }

    private fun Activity?.simpleName() = this?.javaClass?.simpleName ?: "NullActivity"
}
