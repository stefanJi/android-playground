package io.github.stefanji.playground

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt

/**
 * Create by jy on 2019-06-20
 */
class LossOfPlosionSpan(
    @ColorInt private val leftColor: Int,
    @ColorInt private val rightColor: Int,
    @ColorInt private val lineColor: Int,
    private val lineWidthPx: Float,
    private val lineIntervalHeightPx: Float
) : ReplacementSpan() {

    private var w = 0

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val width = Math.round(paint.measureText(text, start, end))
        val metrics = paint.fontMetricsInt
        if (fm != null) {
            fm.top = metrics.top
            fm.ascent = metrics.ascent
            fm.descent = metrics.descent
            fm.bottom = metrics.bottom
        }
        w = width
        return width
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
            paint.isAntiAlias = true

            val cx = x + w / 2
            val s = it.substring(start, end)

            val rightRect = Rect(cx.toInt(), top, (x + w).toInt(), bottom)
            canvas.save()
            canvas.clipRect(rightRect)
            paint.color = leftColor
            canvas.drawText(s, x, y.toFloat(), paint)
            canvas.restore()

            val leftRect = Rect(x.toInt(), top, cx.toInt(), bottom)
            canvas.save()
            canvas.clipRect(leftRect)
            paint.color = rightColor
            canvas.drawText(s, x, y.toFloat(), paint)
            canvas.restore()

            paint.reset()
            paint.strokeWidth = lineWidthPx
            paint.style = Paint.Style.STROKE
            paint.color = lineColor
            paint.pathEffect = DashPathEffect(floatArrayOf(lineIntervalHeightPx, lineIntervalHeightPx), 0f)
            canvas.drawLine(cx, top.toFloat(), cx, bottom.toFloat(), paint)
            paint.reset()
        }
    }
}