package io.github.stefanji.playground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Create by jy on 2019-06-14
 */

private const val TAG = "BellAlarmReceiver"

class BellAlarmReceiver() : BroadcastReceiver() {

    private var onAlarmAlert: (() -> Unit)? = null

    constructor(onAlarmAlert: (() -> Unit)? = null) : this() {
        this.onAlarmAlert = onAlarmAlert
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "receive broadcast: action: ${intent?.action} extras: ${intent?.extras?.toString()}")
        onAlarmAlert?.invoke()
    }

    companion object {
        private const val alarmAlertAction = "android.intent.action.ALARM_CHANGED"

        fun attach(activity: FragmentActivity, onAlarmAlert: (() -> Unit)? = null) {
            val filter = IntentFilter().apply { addAction(alarmAlertAction) }

            val receiver = BellAlarmReceiver(onAlarmAlert)

            val lifeObserver = object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> activity.registerReceiver(receiver, filter)
                        Lifecycle.Event.ON_PAUSE -> activity.unregisterReceiver(receiver)
                        Lifecycle.Event.ON_DESTROY -> activity.lifecycle.removeObserver(this)
                        else -> {
                        }
                    }
                }
            }

            activity.lifecycle.addObserver(lifeObserver)
        }
    }
}