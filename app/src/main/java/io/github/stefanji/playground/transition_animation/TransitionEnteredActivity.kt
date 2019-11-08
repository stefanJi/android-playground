package io.github.stefanji.playground.transition_animation

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import io.github.stefanji.playground.R

/**
 * Create by jy on 2019-11-08
 */
class TransitionEnteredActivity : Activity() {

    companion object {
        const val SHARED_ELEMENT = "btn_enter_exit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
//                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            }
        }

        title = "详情 Activity"

        setContentView(R.layout.activity_transition_entered)

        ViewCompat.setTransitionName(findViewById(R.id.btn_exit), SHARED_ELEMENT)

        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }
    }
}