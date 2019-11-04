package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hot_fix.*

/**
 * Create by jy on 2019-11-02
 */
class HotFixDexActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_fix)

        tv_target.text = "v1"

        btn_apply_fix.setOnClickListener {
            applyFix()
        }
    }

    private fun applyFix() {

    }
}