package io.github.stefanji.playground

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_drawable_test.*

/**
 * Create by jy on 2019-10-25
 */
class DrawableTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable_test)

        var set = false
        load.setOnClickListener {
            iv.setImageDrawable(null)
            val bmp =
                BitmapFactory.decodeResource(resources, R.drawable.bg_level_up_n_alpha, BitmapFactory.Options().apply {
                    this.inPreferredConfig =
                        Bitmap.Config.RGB_565 /* 只有当图片中没有 alpha 通道时有作用, 否则 SDK 内部还是会用 ARGB_8888 加载 */
                })
            log("加载不带 alpha 通道的同一张图, 但是由于不带 alpha 通道了, 图片的圆角就会被默认白色填充. 可以利用 BitmapShader 做圆角效果")
            iv.setImageDrawable(BitmapDrawable(resources, bmp))
            iv.post {
                val drawable = iv.drawable as? BitmapDrawable
                drawable?.let {
                    log("ImageView 加载的 bitmap: ${it.bitmap.toStr()}")
                    log("ImageView 尺寸: ${iv.width} x ${iv.height}")
                }
            }
        }

        /*
        log("screen density: ${Resources.getSystem().displayMetrics.densityDpi}")

        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.ic_bell_recording)

        val option = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        val a = mutableMapOf<String, Any>().mapValues {

        }
        BitmapFactory.decodeResource(resources, R.drawable.ic_bell_recording, option)
        log("option: ${option.toStr()}")
        option.inJustDecodeBounds = false
        val b2 = BitmapFactory.decodeResource(resources, R.drawable.ic_bell_recording, option)
        log("option: ${option.toStr()}")
        log("b2: ${b2.toStr()}")

        val bitmap3 = bitmap1.copy(Bitmap.Config.RGB_565, false)

        log("b1: ${bitmap1.toStr()}")
        log("b3: ${bitmap3.toStr()}")

        iv.background = BitmapDrawable(resources, bitmap3).apply {
            gravity = Gravity.CENTER
        }
        */

        iv.post {
            log("加载带 alpha 通道的图")
            val drawable = iv.drawable as? BitmapDrawable
            drawable?.let {
                log("ImageView 加载的 bitmap: ${it.bitmap.toStr()}")
                log("ImageView 尺寸: ${iv.width} x ${iv.height}")
            }
        }
    }

    private fun log(msg: String) {
        Log.d("DrawableTest", msg)
    }
}

fun Bitmap.toStr() =
    "size: ${this.width} x ${this.height}, bytes: ${this.allocationByteCount}, density: ${this.density}, config: ${this.config}"

fun BitmapFactory.Options.toStr() =
    "size: ${this.outWidth} x ${this.outHeight}, targetDensity: ${this.inTargetDensity}, inDensity: ${this.inDensity}, " +
            "preferredConfig: ${this.inPreferredConfig}, inSampleSize: ${this.inSampleSize}, " +
            "bytes: ${this.outHeight.toFloat() * this.outWidth * this.inPreferredConfig.size() * this.inTargetDensity / this.inDensity}"

fun Bitmap.Config.size() = when (this) {
    Bitmap.Config.ARGB_8888 -> 4
    Bitmap.Config.RGB_565 -> 2
    Bitmap.Config.ARGB_4444 -> 2
    Bitmap.Config.ALPHA_8 -> 1
    else -> 0
}