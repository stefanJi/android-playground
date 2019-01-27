package io.github.stefanji.widgets

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt
import android.text.style.ReplacementSpan

/**
 * <p>
 *   val str = "Underlined Text . Test Test Test Test Test Test Test Test Test Test Test Test"
 *   val content = SpannableString(str)
 *   content.setSpan(DotUnderlingSpan(10f, 5f, 5f, 2f, Color.BLUE), 0, 10, 0)
 *   tvName.text = content
 * </p>
 * @param dotWidth dot width
 * @param intervalWidth interval width
 * @param marginBottom underling margin bottom of text
 * @param lineHeight line block height
 */
class DotUnderlingSpan(
    private val dotWidth: Float,
    private val intervalWidth: Float,
    private val marginBottom: Float,
    private val lineHeight: Float,
    @ColorInt private val color: Int
) : ReplacementSpan() {
    private var textWidth = 0

    private val dotPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = color
        strokeWidth = lineHeight
        pathEffect = DashPathEffect(floatArrayOf(dotWidth, intervalWidth), 0f)
    }

    private val path = Path()

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
            canvas.drawText(it, start, end, x, y.toFloat(), paint)
            path.moveTo(x, y + marginBottom)
            path.lineTo(x + textWidth, y + marginBottom)
            canvas.drawPath(path, dotPaint)
        }
    }
}