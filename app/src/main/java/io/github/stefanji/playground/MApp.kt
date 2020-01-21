package io.github.stefanji.playground

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


/**
 * Create by jy on 2019-05-31
 */
private const val TAG = "MApp"

class MApp : MultiDexApplication() {
    private val lifecycleCallbacks = ActivityLife()

    override fun onCreate() {
        super.onCreate()
//        enableActivityThreadLog()
        MultiDex.install(this)
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}

private fun enableActivityThreadLog() {
    val activityThread = Class.forName("android.app.ActivityThread")
    val currentActivityThreadMethod = activityThread.getDeclaredMethod("currentActivityThread")
    currentActivityThreadMethod.isAccessible = true
    val currentActivityThread = currentActivityThreadMethod.invoke(null)
    activityThread.declaredFields.forEach {
        Log.d("TAG", "${it.name} ${it.type}")
    }
    arrayOf("DEBUG_HW_ACTIVITY", "DEBUG_HW_BROADCAST", "DEBUG_HW_PROVIDER", "DEBUG_HW_SERVICE", "IS_DEBUG_VERSION").forEach {
        val filed = activityThread.getDeclaredField(it)
        filed.isAccessible = true
        filed.set(currentActivityThread, true)
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
