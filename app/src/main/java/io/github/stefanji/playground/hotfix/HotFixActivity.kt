package io.github.stefanji.playground.hotfix

import android.app.Activity
import android.os.Bundle
import io.github.stefanji.playground.JNIRegiester
import io.github.stefanji.playground.R
import kotlinx.android.synthetic.main.ac_hotfix.*

/**
 * Create by jy on 2020-01-21
 */
class HotFixActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_hotfix)
        val target = Target()
        tv.text = target.hello()

        btn.setOnClickListener {
            JNIRegiester.hotfix(
                Target::class.java.getDeclaredMethod("hello"),
                FixPatchForTarget::class.java.getDeclaredMethod("hello")
            )
            tv.text = target.hello()
        }
    }
}