package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

/**
 * Create by jy on 2019-11-02
 */
class LeakTestActivity : Activity() {

    private val byte = ByteArray(1024 * 1024 * 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val xxThread = HandlerThread("BackendThread")
        xxThread.start()
        val handler = Handler(xxThread.looper)
        val obj = this
        // 先向 BackendThread 发送一条 Message, 该 Message 会成为 BackendThread
        handler.post {
            runOnUiThread {
                val messageA = handler.obtainMessage(1, obj)
                Message.obtain(messageA).sendToTarget()
            }
        }
    }
}