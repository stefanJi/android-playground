package io.github.stefanji.playground

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Create by jy on 2019-06-06
 */
class ViewTest @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val p = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rect = RectF(0f, 0f, 100f, 120f)
        val rect2 = Rect(0, 0, 100, 100)

        canvas.drawColor(Color.GREEN)

        p.color = Color.GRAY
        canvas.drawRect(rect2, p)

        p.color = Color.BLUE
        canvas.clipRect(rect2, Region.Op.DIFFERENCE)
        canvas.drawRoundRect(rect, 10f, 10f, p)

        p.color = Color.BLUE
        canvas.drawRect(rect2, p)
    }
}