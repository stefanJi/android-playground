package io.github.stefanji.playground

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Create by jy on 2019-11-08
 */
class FragmentsActivity : AppCompatActivity() {

    private lateinit var btnAdd: Button

    companion object {
        private const val TAG = "MFragmentActivity"

        private fun log(msg: String?) {
            msg?.let { Log.d(TAG, it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("[onCreate] activity")
        setContentView(R.layout.activity_fragments)

        btnAdd = findViewById(R.id.btn_add)

        btnAdd.apply {
            val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    log("[onPreDraw] button")
                    printBtnInfo()
                    if (viewTreeObserver.isAlive) {
                        viewTreeObserver.removeOnPreDrawListener(this)
                    }
                    return true
                }
            }

            viewTreeObserver.addOnPreDrawListener(preDrawListener)

            setOnClickListener {
                with(supportFragmentManager) {
                    beginTransaction()
                        .add(R.id.fragment_container, MFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        findViewById<Button>(R.id.btn_remove).setOnClickListener {
            with(supportFragmentManager) {
                popBackStack()
            }
        }

        printBtnInfo()
    }

    override fun onResume() {
        super.onResume()
        log("[onResume] activity")
        printBtnInfo()
    }

    private fun printBtnInfo() {
        log("[printBtnInfo] width: ${btnAdd.width} height: ${btnAdd.height}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        log("[onSaveInstanceState] $outState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        log("[onRestoreInstance] $savedInstanceState")
    }
}


class MFragment private constructor() : Fragment(R.layout.fragment_test) {

    companion object {
        fun newInstance(): MFragment {
            count++
            return MFragment()
        }

        private var count = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv).text = "No. $count"
    }

    override fun onDestroy() {
        super.onDestroy()
        count--
    }
}