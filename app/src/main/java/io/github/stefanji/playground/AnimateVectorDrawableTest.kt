package io.github.stefanji.playground

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.ac_animate_vector.*

/**
 * Create by jy on 2019-12-13
 */
class AnimateVectorDrawableTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_animate_vector)

        val drawable = AnimatedVectorDrawableCompat.create(this, R.drawable.drawble_complete)
        Log.d("JY", "1: $drawable")
        Log.d("JY", "iv: $iv")
        iv.setImageDrawable(drawable)
        iv.postDelayed(Runnable {
            Log.d("JY", "2: ${iv.drawable}")
            val animate = iv.drawable as? AnimatedVectorDrawableCompat
            animate?.start()

            val bmp = iv.drawable as? BitmapDrawable
            Log.d("JY", "3: $bmp")
        }, 500)
    }
}