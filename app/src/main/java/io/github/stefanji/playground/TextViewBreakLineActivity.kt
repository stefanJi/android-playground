package io.github.stefanji.playground

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Space
import android.widget.TextView
import io.github.stefanji.playground.widget.toPx
import io.github.stefanji.playground.widget.toPxInt

/**
 * Create by jy on 2019-11-25
 */
class TextViewBreakLineActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = ScrollView(this)
        setContentView(contentView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            contentView.addView(this, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }

        arrayOf(
            makeTipTextView("SIMPLE NONE"),
            makeTextView(Layout.BREAK_STRATEGY_SIMPLE, Layout.HYPHENATION_FREQUENCY_NONE),
            makeTipTextView("BALANCED NONE"),
            makeTextView(Layout.BREAK_STRATEGY_BALANCED, Layout.HYPHENATION_FREQUENCY_NONE),
            makeTipTextView("HIGH_QUALITY NONE"),
            makeTextView(Layout.BREAK_STRATEGY_HIGH_QUALITY, Layout.HYPHENATION_FREQUENCY_NONE),

            makeTipTextView("SIMPLE NORMAL"),
            makeTextView(Layout.BREAK_STRATEGY_SIMPLE, Layout.HYPHENATION_FREQUENCY_NORMAL),
            makeTipTextView("BALANCED NORMAL"),
            makeTextView(Layout.BREAK_STRATEGY_BALANCED, Layout.HYPHENATION_FREQUENCY_NORMAL),
            makeTipTextView("HIGH_QUALITY NORMAL"),
            makeTextView(Layout.BREAK_STRATEGY_HIGH_QUALITY, Layout.HYPHENATION_FREQUENCY_NORMAL),

            makeTipTextView("SIMPLE FULL"),
            makeTextView(Layout.BREAK_STRATEGY_SIMPLE, Layout.HYPHENATION_FREQUENCY_FULL),
            makeTipTextView("BALANCED FULL"),
            makeTextView(Layout.BREAK_STRATEGY_BALANCED, Layout.HYPHENATION_FREQUENCY_FULL),
            makeTipTextView("HIGH_QUALITY FULL"),
            makeTextView(Layout.BREAK_STRATEGY_HIGH_QUALITY, Layout.HYPHENATION_FREQUENCY_FULL)
        ).forEachIndexed { index, it ->
            layout.addView(it)
            layout.addView(
                Space(this@TextViewBreakLineActivity),
                LinearLayout.LayoutParams(WRAP_CONTENT, index % 2 * 20.toPxInt())
            )
        }
    }

    private fun makeTextView(breakStrategy: Int, hyphenationFrequency: Int): TextView {
        val mTypeFace = Typeface.createFromAsset(assets, "font/GilroyMedium.otf")
        val params = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            setMargins(16.toPxInt(), 0, 16.toPxInt(), 0)
        }
        return TextView(this).apply {
            text = "Did you have a good time?"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            gravity = Gravity.CENTER_HORIZONTAL
            typeface = mTypeFace
            layoutParams = params
            setLineSpacing(16.toPx(), lineSpacingMultiplier)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.breakStrategy = breakStrategy
                this.hyphenationFrequency = hyphenationFrequency
            }
        }
    }

    private fun makeTipTextView(tip: String): TextView {
        return TextView(this).apply {
            text = tip
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
    }
}
