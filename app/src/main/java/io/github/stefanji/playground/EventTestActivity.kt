package io.github.stefanji.playground

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_event_test.*
import kotlin.math.abs

/**
 * Create by jy on 2019-11-03
 */
class EventTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_test)
        title = "滑动冲突测试"

        val parent = MViewPage(this, null).apply {}
        parent.setBackgroundColor(Color.BLACK)

        val childs = mutableListOf<View>()

        repeat(2) {
            Child(this, null, 0)
                .apply {
                    text = "$it"
                    layoutParams = ViewGroup.LayoutParams(50, 200)
                    setBackgroundColor(Color.RED)
                }.let(childs::add)
        }

        val listView = MListView(this, null, 0)
        listView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        listView.adapter =
            ArrayAdapter(
                this, android.R.layout.simple_list_item_1, android.R.id.text1,
                arrayOf("A", "b", "c", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d")
            )
        listView.setBackgroundColor(Color.BLUE)
        childs.add(listView)

        parent.adapter = MPageAdapter(childs)

        content.addView(parent)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TAG123", "onTouchEvent activity: ${event.action.actionName()}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.d("tag123", "===========")
        Log.d("tag123", "activity: dispatch ${event.action.actionName()}")
        tv_event.text = event.action.actionName()
        return super.dispatchTouchEvent(event)
    }
}

class Child(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : Button(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TAG123", "onTouchEvent child: ${event.action.actionName()}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.d("tag123", "child: dispatch ${event.action.actionName()}")
        return super.dispatchTouchEvent(event)
    }
}

fun Int.actionName(): String = when (this) {
    MotionEvent.ACTION_MOVE -> "move"
    MotionEvent.ACTION_CANCEL -> "cancel"
    MotionEvent.ACTION_DOWN -> "down"
    MotionEvent.ACTION_UP -> "up"
    else -> ""
}

class MViewPage(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var lx = 0f
    var ly = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "ViewPager intercept ${ev.action.actionName()}")
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                super.onInterceptTouchEvent(ev)
                lx = ev.x
                ly = ev.y
                false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - lx
                val dy = ev.y - ly
                lx = ev.x
                ly = ev.y

                return abs(dx) > abs(dy)
            }
            else -> {
                lx = ev.x
                ly = ev.y
                false
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "ViewPager dispatch ${ev.action.actionName()}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "ViewPager onTouchEvent ${ev.action.actionName()}")
        return super.onTouchEvent(ev)
    }
}

class MPageAdapter(private val data: List<View>) : PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return data[position].apply { container.addView(data[position]) }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}

class MListView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ListView(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "MListView intercept ${ev.action.actionName()}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "MListView dispatch ${ev.action.actionName()}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("tag123", "MListView onTouchEvent ${ev.action.actionName()}")
        return super.onTouchEvent(ev)
    }
}
