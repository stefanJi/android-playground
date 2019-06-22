package io.github.stefanji.playground

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_3.*


class MainActivity : AppCompatActivity() {

    private lateinit var hostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val content = (inflater.inflate(R.layout.activity_1, null) as ViewGroup)
        setContentView(content)
        content.addView(inflater.inflate(layoutId(), null))
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        progressBar.progressDrawable.apply {
            highLightColor = ContextCompat.getColor(applicationContext, R.color.bell_comflower)
            intervalHighLightColor = ContextCompat.getColor(applicationContext, R.color.bell_comflower_al)
            intervalColor = Color.BLACK
            radius = 3.toPxInt().toFloat()
            intervalWidth = 2.toPx()
            progress = 2
        }
//        progressBar.setProgress(1)
        progressBar.postDelayed({
            progressBar.progressDrawable.progress = 5
        }, 200)
        BellAlarmReceiver.attach(this)
        val t = "0123456789"
        val a = SpannableString(t).apply {
            setSpan(
                HalfSpan(
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    2.toPx(),
                    2.toPx()
                ),
                5,
                6,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )

            setSpan(
                HalfSpan(
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    2.toPx(),
                    2.toPx()
                ),
                6,
                7,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )

            setSpan(
                HalfSpan(
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    1.toPx(),
                    2.toPx()
                ),
                8,
                9,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
        }

        tv.text = a
    }

    fun layoutId() = R.layout.activity_3

    fun huaWeiHasNotchInScreen(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.getClassLoader()
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
        } finally {
            return ret
        }
    }

    fun getNotchSize(context: Context): IntArray {
        var ret = intArrayOf(0, 0)
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(HwNotchSizeUtil) as IntArray
        } catch (e: ClassNotFoundException) {
            Log.e("test", "getNotchSize ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "getNotchSize NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("test", "getNotchSize Exception")
        } finally {
            return ret
        }

    }
}