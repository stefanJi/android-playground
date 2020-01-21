package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * Create by jy on 2019-12-16
 */
class HandleOOMActivity : Activity() {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("OOM", "[onCreate] defaultHandler: $defaultHandler")

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("OOM", "unCaughtException: $t , err:${e.message}")
        }

        object : Thread() {
            override fun run() {
                val bytes = ByteArray(Runtime.getRuntime().maxMemory().toInt())
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OOM", "[onDestroy] called")
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler)
    }
}