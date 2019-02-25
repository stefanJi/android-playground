package io.github.stefanji.widgets

import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import androidx.annotation.ColorInt


/*
for fix: ForegroundColorSpan not work when with ReplacementSpan
 */
class FrontColorSpan(@ColorInt val mColor: Int) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.color = mColor
    }

    override fun updateDrawState(tp: TextPaint?) {
        tp?.color = mColor
    }
}