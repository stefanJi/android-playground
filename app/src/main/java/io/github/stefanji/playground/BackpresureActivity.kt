package io.github.stefanji.playground

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Create by jy on 2019-12-05
 */
class BackpresureActivity : Activity() {
    var sub: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val frameLayout = FrameLayout(this).apply {
            setBackgroundColor(Color.GRAY)
        }

        setContentView(
            frameLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        val publisher = PublishProcessor.create<Int>()
        publisher
            .onBackpressureLatest()
            .observeOn(Schedulers.io())
            .subscribe(object : Subscriber<Int> {
                override fun onComplete() {
                    Log.d("jy", "complete count: $count")
                }

                override fun onSubscribe(s: Subscription) {
                    sub = s
                    s.request(Long.MAX_VALUE)
                }

                private var count = 0
                override fun onNext(t: Int) {
                    count++
                    Log.d("jy", "onNext: $t")
//                    this@BackpresureActivity.toString()
//                    sub?.request(1)
                    Thread.sleep(5)
                }

                override fun onError(t: Throwable) {
                    Log.e("jy", t.message)
                    Log.d("jy", "count: $count")
                    t.printStackTrace()
                }
            })

        frameLayout.postDelayed(Runnable {
            repeat(2_20) {
                publisher.onNext(it)
            }
            publisher.onComplete()
        }, 1000)

        /*

        val observable = Observable.create<ByteArray> { emmiter ->
            repeat(1_000_000) {
                emmiter.onNext(ByteArray(1024))
            }
        }


        frameLayout.postDelayed(Runnable {
            observable
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d("JY", "received ${it.size}")
                    Thread.sleep(1000)
                }
            observable
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d("JY", "received ${it.size}")
                }
        }, 1000)
         */
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}