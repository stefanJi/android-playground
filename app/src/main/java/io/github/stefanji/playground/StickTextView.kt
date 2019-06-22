package io.github.stefanji.playground

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Create by jy on 2019-06-20
 */
class StickTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()


    init {
        paint.apply {

        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

}