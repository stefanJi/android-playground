package io.github.stefanji.playground.transition_animation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.core.app.ActivityOptionsCompat
import io.github.stefanji.playground.R

/**
 * Create by jy on 2019-11-08
 */
class TransitionEnterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
//                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            }
        }

        title = "入口 Activity"

        setContentView(R.layout.activity_transition_enter)

        findViewById<Button>(R.id.btn_enter).setOnClickListener {
            val intent = Intent(this, TransitionEnteredActivity::class.java)
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, it, TransitionEnteredActivity.SHARED_ELEMENT)
            startActivity(intent, options.toBundle())
        }
    }
}