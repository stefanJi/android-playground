package io.github.stefanji.playground

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar

fun Int.toPxInt() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density)

/**
 * Create by jy on 2019-06-06
 * 带刻度的进度条
 */
class IntervalProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private var mProgressDrawable: IntervalProgressDrawable? = null

    override fun setProgress(progress: Int) {
        progressDrawable.progress = progress
    }

    override fun setMax(max: Int) {
        progressDrawable.max = max
    }

    override fun getProgressDrawable(): IntervalProgressDrawable {
        if (mProgressDrawable == null) {
            mProgressDrawable = IntervalProgressDrawable()
            progressDrawable = mProgressDrawable!!
        }
        return mProgressDrawable!!
    }
}

class IntervalProgressDrawable : Drawable() {

    /**
     * 刻度数量
     */
    var max: Int = 1
        set(value) {
            if (max < 1) {
                throw IllegalStateException("max must >= 1")
            }
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 当前刻度
     */
    var progress: Int = 0
        set(value) {
            if (value != field) {
                if (progress < 0 || progress > max) {
                    throw IllegalStateException("progress must <= max and > 0")
                }
                field = value
                invalidateSelf()
            }
        }

    /**
     * 刻度高亮色
     */
    var highLightColor: Int = Color.RED
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 刻度默认色
     */
    var defaultColor: Int = Color.GRAY
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 刻度之间间隔色
     */
    var intervalColor: Int = Color.TRANSPARENT
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 刻度之间间隔的高亮色，当在进度时使用该颜色
     */
    var intervalHighLightColor: Int = Color.TRANSPARENT
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 刻度之间间隔的宽
     */
    var intervalWidth: Float = 2.toPx()
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 头尾刻度的圆角度数
     */
    var radius: Float = 10.toPxInt().toFloat()
        set(value) {
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    private var progressWidth = 0f
    private var widgetHeight = 0

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = defaultColor
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        widgetHeight = bounds.height()
        progressWidth = (bounds.width() - intervalWidth * (max - 1)) / max
    }

    override fun draw(canvas: Canvas) {
        repeat(max) {
            val dx = (progressWidth + intervalWidth) * it
            val inProgress = it < progress
            val isHighlightRule = it < progress - 1
            val drawLeftRound = it == 0 && radius > 0
            val drawRightRound = it == max - 1 && radius > 0
            val isLastRule = it == max - 1

            canvas.save()
            canvas.translate(dx, 0f)

            paint.color = if (inProgress) highLightColor else defaultColor

            val rect = RectF(0f, 0f, progressWidth, widgetHeight.toFloat())

            if (drawLeftRound) {
                drawLeftRound(canvas, rect)
            }

            if (drawRightRound) {
                drawRightRound(canvas, rect)
            }

            canvas.drawRect(rect, paint)

            paint.color = if (isHighlightRule) intervalHighLightColor else intervalColor
            if (!isLastRule && paint.color != Color.TRANSPARENT) {
                canvas.drawRect(
                    progressWidth,
                    0f,
                    progressWidth + intervalWidth,
                    widgetHeight.toFloat(),
                    paint
                )
            }

            canvas.restore()
        }
    }

    private fun drawLeftRound(canvas: Canvas, rectF: RectF) {
        val r = RectF(radius, 0f, progressWidth, widgetHeight.toFloat())
        drawRadius(canvas, r, rectF)
        rectF.left += radius
    }

    private fun drawRightRound(canvas: Canvas, rectF: RectF) {
        val r = RectF(0f, 0f, progressWidth - radius, widgetHeight.toFloat())
        drawRadius(canvas, r, rectF)
        rectF.right -= radius
    }

    private fun drawRadius(canvas: Canvas, clip: RectF, rectF: RectF) {
        canvas.save()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutRect(clip)
        } else {
            canvas.clipRect(clip, Region.Op.DIFFERENCE)
        }
        canvas.drawRoundRect(rectF, radius, radius, paint)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter) {
        paint.colorFilter = colorFilter
    }
}