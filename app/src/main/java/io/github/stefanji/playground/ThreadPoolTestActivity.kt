package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Create by jy on 2019-11-15
 */
class ThreadPoolTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pool = ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, ArrayBlockingQueue(10), object : ThreadFactory {
            val counter = AtomicInteger()
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "MThread-${counter.incrementAndGet()}")
            }
        })

        val button = Button(this).apply {
            text = "Start Pool"
            setOnClickListener {
                // submit 创建的 FutureTask 会将异常 catch 后存下来
                pool.submit(ExceptionRunnable())
                // execute 创建的 Worker 会 catch 到异常后继续抛出来, 如果外部没有 catch, 就会导致应用 crash
                pool.execute(ExceptionRunnable())
            }
        }

        addContentView(
            button,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
    }
}

class ExceptionRunnable : Runnable {
    override fun run() {
        Log.d("ThreadPoolTest", "throw a exception")
        throw RuntimeException("Just a test exception")
    }
}