package io.github.stefanji.widgets

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorInt

class DotBottomSpan(private val tv: TextView) : ReplacementSpan() {

    private var textWidth = 0
    private val dotPaint = Paint()

    var dotInterval: Float = 5f
        set(value) {
            if (value != field) {
                tv.invalidate()
            }
        }

    @ColorInt
    var dotColor: Int = Color.RED
        set(value) {
            if (value != field) {
                setupPaint()
                tv.invalidate()
            }
        }

    var dotRadius: Float = 5f
        set(value) {
            if (value != field) {
                tv.invalidate()
            }
        }

    private fun setupPaint() {
        dotPaint.apply {
            style = Paint.Style.FILL
            color = dotColor
        }
    }

    init {
        setupPaint()
    }

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        textWidth = text?.let {
            paint.measureText(it, start, end).toInt()
        } ?: 0
        return textWidth
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        text?.let {
            Log.d("TAG", it.toString())
            canvas.drawText(it, start, end, x, y.toFloat(), paint)
            val w = paint.measureText(it.subSequence(start, end).toString())
            canvas.save()
            val cx = (x + w) / 2
            canvas.drawCircle(cx, y + dotInterval, dotRadius, dotPaint)
            canvas.restore()
        }
    }
}