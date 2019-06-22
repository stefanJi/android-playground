package io.github.stefanji.widgets

import android.graphics.Color

/**
 * Create by jy on 2019-05-21
 */
object ColorUtil {

    @JvmStatic
    fun combineColors(color1: Int, color2: Int, alpha: Float): Int {
        fun createSingleColor(single: Int, single2: Int, alpha: Float): Int {
            return (single * (1 - alpha) + single2 * alpha).toInt()
        }

        val r = Color.red(color1)
        val g = Color.green(color1)
        val b = Color.blue(color1)

        val r2 = Color.red(color2)
        val g2 = Color.green(color2)
        val b2 = Color.blue(color2)

        return Color.rgb(
            createSingleColor(r, r2, alpha),
            createSingleColor(g, g2, alpha),
            createSingleColor(b, b2, alpha)
        )
    }

    @JvmStatic
    fun blendColor(fg: Int, bg: Int): Int {
        val scr = Color.red(fg)
        val scg = Color.green(fg)
        val scb = Color.blue(fg)
        val sa = fg.ushr(24)
        val dcr = Color.red(bg)
        val dcg = Color.green(bg)
        val dcb = Color.blue(bg)
        val r = dcr * (0xff - sa) / 0xff + scr * sa / 0xff
        val g = dcg * (0xff - sa) / 0xff + scg * sa / 0xff
        val b = dcb * (0xff - sa) / 0xff + scb * sa / 0xff
        return (r shl 16) + (g shl 8) + b or -0x1000000
    }
}