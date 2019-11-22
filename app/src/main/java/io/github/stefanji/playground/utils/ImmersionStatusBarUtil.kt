package io.github.stefanji.playground.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.SparseBooleanArray
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils

/**
 * Create by jy on 2019-05-21
 */

object ImmersionStatusBarUtil {
    var SYSTEM_WINDOW_INSERT_TOP_CACHE = 0
    private val DARK_COLOR_CACHE = SparseBooleanArray()

    /**
     * 支持 5.0 及以上系统为沉浸式状态栏
     * 5.0 系统由于状态栏文字颜色为白色且无法修改, 故只支持深色颜色的沉浸式状态栏.
     * 6.0 及以上系统: 如果设置深色, 文字就是白色; 否则文字就是黑色
     * @param colorInt 状态栏颜色
     */
    @JvmOverloads
    @JvmStatic
    fun setColor(activity: Activity, @ColorInt colorInt: Int, fitsSystemWindow: Boolean = true) {
        setColor(activity.window, colorInt)
        rootViewFitSystemWindow(activity, fitsSystemWindow)
    }

    @JvmStatic
    fun setColor(window: Window, @ColorInt color: Int) {
        val isDark = isDarkColor(color)
        window.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && !isDark) {
                clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = color
                lightStatusBar(decorView, isDark)
            }
        }
    }

    @JvmStatic
    fun lightStatusBar(decorView: View, isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = if (isDark) {
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    @JvmStatic
    private fun isDarkColor(color: Int): Boolean {
        val exist = DARK_COLOR_CACHE.indexOfKey(color) >= 0
        if (exist) {
            return DARK_COLOR_CACHE[color]
        }
        val isDark = ColorUtils.calculateLuminance(color) < 0.5
        DARK_COLOR_CACHE.put(color, isDark)
        return isDark
    }

    /**
     * 6.0 及以上状态栏覆盖在内容上
     * @param offsetViews 需要向下移动状态栏高度的 views
     * @param color statusBar 的颜色, 不传为透明
     */
    @JvmStatic
    @JvmOverloads
    fun overlay(
        activity: Activity,
        color: Int = Color.TRANSPARENT,
        vararg offsetViews: View? = emptyArray(),
        hideStatusBarContent: Boolean = false
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        setColor(activity, color, false)
        overlay(activity.window, color, *offsetViews, hideStatusBarContent = hideStatusBarContent)
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun overlay(
        window: Window,
        color: Int = Color.TRANSPARENT,
        vararg offsetViews: View? = emptyArray(),
        hideStatusBarContent: Boolean = false
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        setColor(window, color)
        with(window.decorView) {
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN: Activity 全屏显示，但状态栏不会被隐藏，状态栏依然可见，Activity 顶端布局部分会被状态栏遮住
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (hideStatusBarContent) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
                // 全屏布局且隐藏状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        offsetViews.forEach {
            it?.doOnApplyWindowInsets { v, insets, _, initMargin ->
                v.updateMargin(top = initMargin.top + insets.systemWindowInsetTop)
            }
        }
    }

    @JvmStatic
    private fun rootViewFitSystemWindow(activity: Activity, fitSystemWindow: Boolean) {
        val parent = activity.findViewById(android.R.id.content) as ViewGroup
        val root = parent.getChildAt(0)
        root?.fitsSystemWindows = fitSystemWindow
        if (!fitSystemWindow) {
            root?.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * 8.0 及以上设置 NavigationBar 背景色, 同时当背景色为亮色时设置模式为 SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
     */
    @JvmStatic
    fun setNavigationBarColor(window: Window, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = color
            if (!isDarkColor(color)) {
                with(window.decorView) {
                    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
inline fun View.doOnApplyWindowInsets(crossinline f: (View, WindowInsets, initPadding: InitialData, initMargin: InitialData) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = InitialData(paddingLeft, paddingTop, paddingRight, paddingBottom)
    val initialMargin = (layoutParams as ViewGroup.MarginLayoutParams).let {
        InitialData(it.leftMargin, it.topMargin, it.rightMargin, it.bottomMargin)
    }
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        ImmersionStatusBarUtil.SYSTEM_WINDOW_INSERT_TOP_CACHE = insets.systemWindowInsetTop
        f(v, insets, initialPadding, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

data class InitialData(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.updateMargin(
    layout: ViewGroup.MarginLayoutParams = (layoutParams as ViewGroup.MarginLayoutParams),
    left: Int = layout.leftMargin,
    top: Int = layout.topMargin,
    right: Int = layout.rightMargin,
    bottom: Int = layout.bottomMargin
) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(left, top, right, bottom)
}