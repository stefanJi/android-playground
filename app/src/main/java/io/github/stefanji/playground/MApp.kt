package io.github.stefanji.playground

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Create by jy on 2019-05-31
 */

class MApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}