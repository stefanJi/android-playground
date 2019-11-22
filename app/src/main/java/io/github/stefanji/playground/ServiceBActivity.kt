package io.github.stefanji.playground

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button

/**
 * Create by jy on 2019-11-21
 */
class ServiceBActivity : Activity() {

    private var connection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val msr = Messenger(service)
                msr.send(Message.obtain())
            }
        }

        Button(this).apply {
            text = "BindService"
            setOnClickListener {
                bindService(
                    Intent(this@ServiceBActivity, RemoteService::class.java),
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
