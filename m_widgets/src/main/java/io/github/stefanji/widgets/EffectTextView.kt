package io.github.stefanji.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView

class EffectTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    interface Effect {
        fun draw(tv: TextView, canvas: Canvas, start: Int, end: Int, text: CharSequence)
    }

    data class EffectData(val effect: Effect, val start: Int, val end: Int)

    private val effects = arrayListOf<EffectData>()
    fun addEffect(effect: Effect, start: Int, end: Int) {
        effects.add(EffectData(effect, start, end))
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        effects.forEach { it.effect.draw(this, canvas, it.start, it.end, text) }
    }
}


class DashedUnderlineEffect : EffectTextView.Effect {
    private val mPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10f
        pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
    }
    private val mPath = Path()

    override fun draw(tv: TextView, canvas: Canvas, start: Int, end: Int, text: CharSequence) {
        val leftWidth = mPaint.measureText(text.substring(0, start))
        val textWidth = mPaint.measureText(text.substring(start, end))
        Log.d("TAG", "${tv.x} $leftWidth $textWidth")
        mPath.moveTo(tv.x + leftWidth, tv.y)
        mPath.lineTo(tv.x + leftWidth + textWidth, tv.y - 50f)
        canvas.drawPath(mPath, mPaint)
        canvas.drawCircle(5f, 5f, 10f, mPaint)
    }

}

class DotBottomEffect : EffectTextView.Effect {
    private val mPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10f
    }

    override fun draw(tv: TextView, canvas: Canvas, start: Int, end: Int, text: CharSequence) {

    }

}