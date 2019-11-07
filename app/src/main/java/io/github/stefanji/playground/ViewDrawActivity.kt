package io.github.stefanji.playground

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout

/**
 * Create by jy on 2019-11-06
 */
class ViewDrawActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "View绘制流程"

        val btn = MButton(this)
            .apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 200)
                text = "Invalidate"
                setOnClickListener {
                    this.invalidate()
                }
            }
        val frame = FrameLayout(this)
        frame.addView(btn)
        setContentView(frame)
    }
}

private class MButton(context: Context) : Button(context) {

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }
}