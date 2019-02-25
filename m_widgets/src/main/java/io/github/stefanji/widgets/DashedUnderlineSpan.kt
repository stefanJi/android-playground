package io.github.stefanji.widgets

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt

/**
 * <p>
 *   val str = "Underlined Text . Test Test Test Test Test Test Test Test Test Test Test Test"
 *   val content = SpannableString(str)
 *   content.setSpan(DashedUnderlineSpan(10f, 2f, 5f, Color.RED, 10f), 4, 7, 0)
 *   tvName.text = content
 * </p>
 * @param dotWidth dot width
 * @param intervalWidth interval width
 * @param lineHeight line block height
 * @param lineColor line color
 * @param marginBottom underling margin bottom of text
 */
class DashedUnderlineSpan(
    private val dotWidth: Float,
    private val intervalWidth: Float,
    private val lineHeight: Float,
    @ColorInt private val lineColor: Int,
    private val marginBottom: Float
) : ReplacementSpan() {
    private var textWidth = 0

    private val dotPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = lineColor
        strokeWidth = lineHeight
        pathEffect = DashPathEffect(floatArrayOf(dotWidth, intervalWidth), 0f)
    }

    private val path = Path()

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        textWidth = text?.let { paint.measureText(it, start, end).toInt() } ?: 0
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