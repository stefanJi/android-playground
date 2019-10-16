package io.github.stefanji.playground

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.Property
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
        progressDrawable.setProgress(progress)
    }

    override fun setProgress(progress: Int, animate: Boolean) {
        progressDrawable.setProgress(progress, animate)
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

    override fun getProgress(): Int {
        return progressDrawable.progress
    }

}

class IntervalProgressDrawable : Drawable() {
    companion object {
        private const val ANIMATE_DURATION = 300L
    }

    //region filed
    /**
     * 刻度数量
     */
    var max: Int = 1
        set(value) {
            check(max >= 1) { "max must >= 1" }
            if (value != field) {
                field = value
                invalidateSelf()
            }
        }

    var progress = 0

    /**
     * 当前刻度
     */
    fun setProgress(progress: Int, animate: Boolean = false) {
        check(!(progress < 0 || progress > max)) { "progress must <= max and > 0" }
        this.progress = progress
        this.isAnimate = animate
        if (animate) {
            if (!bounds.isEmpty) {
                onBoundsChange(bounds)
            }else{
                invalidateSelf()
            }
        }
        invalidateSelf()
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

    var animateDuration = ANIMATE_DURATION

    private var progressWidth = 0f
    private var widgetHeight = 0
    private var isAnimate = false
    private var animateWidth = 0f
    //endregion

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = defaultColor
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        widgetHeight = bounds.height()
        progressWidth = (bounds.width() - intervalWidth * (max - 1)) / max
        Log.d("TAG", "onBoundsChange $progressWidth")

        val start = if (progress == 1 || progress == max) radius else 0f
        ObjectAnimator.ofFloat(this, animateProperty, start, progressWidth)
            .setDuration(animateDuration)
            .apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        isAnimate = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        isAnimate = false
                    }
                })
            }.start()
    }

    override fun draw(canvas: Canvas) {
        Log.d("TAG", "draw")
        drawBackground(canvas)
        drawProgress(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        repeat(max) {
            val dx = (progressWidth + intervalWidth) * it
            val drawLeftRound = it == 0 && radius > 0
            val drawRightRound = it == max - 1 && radius > 0
            val isLastRule = it == max - 1

            canvas.save()
            canvas.translate(dx, 0f)

            paint.color = defaultColor

            val rect = RectF(0f, 0f, progressWidth, widgetHeight.toFloat())

            if (drawLeftRound) {
                drawLeftRound(canvas, rect, progressWidth)
            }

            if (drawRightRound) {
                drawRightRound(canvas, rect, progressWidth)
            }

            canvas.drawRect(rect, paint)

            paint.color = intervalColor
            if (!isLastRule) {
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

    private fun drawProgress(canvas: Canvas) {
        val p = if (isAnimate) (if (progress == 0) 0 else progress - 1) else progress
        repeat(p) {
            val dx = (progressWidth + intervalWidth) * it
            val drawLastInterval = it < progress - 1
            val drawLeftRound = it == 0 && radius > 0
            val drawRightRound = it == max - 1 && radius > 0
            val isLastRule = it == max - 1

            canvas.save()
            canvas.translate(dx, 0f)

            paint.color = highLightColor

            val rect = RectF(0f, 0f, progressWidth, widgetHeight.toFloat())

            if (drawLeftRound) {
                drawLeftRound(canvas, rect, progressWidth)
            }

            if (drawRightRound) {
                drawRightRound(canvas, rect, progressWidth)
            }

            canvas.drawRect(rect, paint)

            paint.color = intervalHighLightColor
            if (drawLastInterval && !isLastRule) {
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

        if (isAnimate) {
            drawLastProgressBlock(canvas)
        }
    }

    private fun drawLastProgressBlock(canvas: Canvas) {
        val dx = (progressWidth + intervalWidth) * (progress - 1)
        val drawLeftRound = progress == 1 && radius > 0
        val drawRightRound = progress == max && radius > 0
        paint.color = highLightColor

        canvas.save()
        canvas.translate(dx, 0f)

        val rect = RectF(0f, 0f, animateWidth, widgetHeight.toFloat())

        if (drawLeftRound) {
            drawLeftRound(canvas, rect, animateWidth)
        }

        if (drawRightRound) {
            drawRightRound(canvas, rect, animateWidth)
        }

        if (drawLeftRound) {
            if (animateWidth >= radius) {
                canvas.drawRect(rect, paint)
            }
        } else {
            canvas.drawRect(rect, paint)
        }

        canvas.restore()
    }

    private fun drawLeftRound(canvas: Canvas, rectF: RectF, blockWidth: Float) {
        val r = RectF(radius, 0f, blockWidth, widgetHeight.toFloat())
        drawRadius(canvas, r, rectF)
        rectF.left += radius
    }

    private fun drawRightRound(canvas: Canvas, rectF: RectF, blockWidth: Float) {
        val r = RectF(0f, 0f, blockWidth - radius, widgetHeight.toFloat())
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

    private val animateProperty by lazy(LazyThreadSafetyMode.NONE) {
        object : Property<IntervalProgressDrawable, Float>(Float::class.java, "AnimateWidth") {
            override fun get(obj: IntervalProgressDrawable): Float = obj.animateWidth

            override fun set(obj: IntervalProgressDrawable, value: Float) {
                obj.animateWidth = value
                obj.invalidateSelf()
            }
        }
    }
}