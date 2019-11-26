package io.github.stefanji.playground.spans

import android.graphics.*
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
    private val singleIntervalHeightPx: Float,
    private val doubleIntervalHeightPx: Float
) : ReplacementSpan() {

    private var w = 0
    private val path = Path()
    private var fontMetrics: Paint.FontMetricsInt? = null

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val width = paint.measureText(text, start, end)
        w = width.toInt()
        if (fm != null) {
            fontMetrics = fm
        }
        return width.toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.isAntiAlias = true

        val cx = x + w / 2
        val s = text.substring(start, end)

        paint.color = rightColor
        val rightRect = Rect(cx.toInt(), top, (x + w).toInt(), bottom)
        canvas.save()
        canvas.clipRect(rightRect)
        canvas.drawText(s, x, y.toFloat(), paint)
        canvas.restore()

        val leftRect = Rect(x.toInt(), top, cx.toInt(), bottom)
        paint.color = leftColor
        canvas.save()
        canvas.clipRect(leftRect)
        canvas.drawText(s, x, y.toFloat(), paint)
        canvas.restore()

        paint.apply {
            reset()
            strokeWidth = lineWidthPx
            style = Paint.Style.STROKE
            color = lineColor
            pathEffect = DashPathEffect(floatArrayOf(singleIntervalHeightPx, doubleIntervalHeightPx), 0f)
        }

        val mBottom = if (fontMetrics != null) fontMetrics!!.bottom + y else bottom
        path.apply {
            rewind()
            moveTo(cx, top.toFloat())
            lineTo(cx, mBottom.toFloat())
        }
        canvas.drawPath(path, paint)
        paint.xfermode = null
    }
}
