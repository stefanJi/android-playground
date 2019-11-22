package io.github.stefanji.playground

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button

/**
 * Create by jy on 2019-11-21
 */
class ServiceAActivity : Activity() {

    private var connection: ServiceConnection? = null

    private val activityMessenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Log.d("TAG", "[handleMessage] activity")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val msr = Messenger(service)
                msr.send(Message.obtain().apply {
                    replyTo = activityMessenger
                })
            }
        }

        Button(this).apply {
            text = "BindService"
            setOnClickListener {
                bindService(
                    Intent(this@ServiceAActivity, RemoteService::class.java),
                    connection!!,
                    Context.BIND_AUTO_CREATE
                )
            }
            addContentView(this, ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection?.let(this::unbindService)
    }
}

class RemoteService : Service() {

    val remoteMessenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Log.d("TAG", "process: ${Process.myPid()}")
        }
    })

    override fun onBind(intent: Intent?): IBinder? {
        return remoteMessenger.binder
    }

    override fun onCreate() {
        Log.d("TAG", "[onCreate]")
        super.onCreate()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.d("TAG", "[onStart]")
        super.onStart(intent, startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG", "[onStartCommand]")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("TAG", "[onDestroy]")
        super.onDestroy()
    }
}