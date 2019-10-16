package io.github.stefanji.playground

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Create by jy on 2019-05-31
 */

class MApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...
    }
}