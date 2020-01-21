package io.github.stefanji.playground

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * Create by jy on 2019-12-20
 */
private const val ACTION_FORCE_STOP_RESCHEDULE = "ACTION_FORCE_STOP_RESCHEDULE"

class CheckAppForceStop : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent()
        intent.component = ComponentName(this, BroadcastReceiver::class.java)
        intent.action = ACTION_FORCE_STOP_RESCHEDULE
        val pendingIntent = PendingIntent.getBroadcast(this, -1, intent, PendingIntent.FLAG_NO_CREATE)

    }

    class BR : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ACTION_FORCE_STOP_RESCHEDULE) {
                    Log.d("CAFS", "received force stop broadcast")
                }
            }
        }
    }
}