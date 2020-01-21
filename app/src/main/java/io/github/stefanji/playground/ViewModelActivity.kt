package io.github.stefanji.playground

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

/**
 * Create by jy on 2020-01-11
 */
class ViewModelActivity : ComponentActivity() {

    private lateinit var mMode: MMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMode = ViewModelProvider(this).get(MMode::class.java)
        Log.d("TAG", "number: ${mMode.number}")

        val parent = FrameLayout(this)

        val textView = TextView(this)
            .apply {
                text = "Number: ${mMode.number}"
                gravity = Gravity.CENTER
            }
        setContentView(parent, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        AsyncLayoutInflater(this).inflate(
            R.layout.item_list,
            parent
        ) { view, resid, p ->
            p?.addView(view)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("TAG", "onRestoreInstanceState $savedInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TAG", "onSaveInstanceState $outState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "ondestroy")
    }

    class MMode : ViewModel {
        constructor() {
            Log.d("TAG", "<init>")
        }

        val number = Random().nextInt(10)
        override fun toString(): String {
            return "MMode(number=$number)"
        }

        override fun onCleared() {
            super.onCleared()
            Log.d("TAG", "onCleared")
        }
    }
}