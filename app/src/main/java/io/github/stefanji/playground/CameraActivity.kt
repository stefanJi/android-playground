package io.github.stefanji.playground

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Create by jy on 2019-10-10
 */
class CameraActivity : FragmentActivity(), FrameProcessor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        /*
        cameraView.setLifecycleOwner(this)
        cameraView.addFrameProcessor(this)

        switch_beauty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cameraView.filter.onDestroy()
                cameraView.filter = BeautyFilter()
            } else {
                cameraView.filter.onDestroy()
                cameraView.filter = NoFilter()
            }
        }
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
         */

//        val queue = LinkedBlockingQueue<MMessage>()
//        queue.offer(MMessage("My message", this))
//        object : Thread("JY-Thread") {
//            override fun run() {
//                while (true) {
//                    val msg = queue.take()
//                    Log.d("TAG123", msg.msg)
//                    Log.d("TAG123", "null: ${msg == null}")
//                }
//            }
//        }.start()
        val a = HandlerThread("JY-HandlerThread")
        a.start()
        val m = Message.obtain().apply {
            obj = this@CameraActivity
            what = 100
        }
        val handler = object : Handler(a.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d("TAG123", msg.what.toString())
            }
        }
        handler.sendMessage(Message.obtain(m))
    }

    private var updateTime = 0L
    private var fps = 0f
    private var times = 0

    override fun process(frame: Frame) {
        val currTime = System.currentTimeMillis()
        if (updateTime == 0L) {
            updateTime = currTime
        }
        val diff = currTime - updateTime
        if (diff > 1000) {
            fps = (times.toFloat() / diff) * 1000f
            updateTime = currTime
            times = 0
        }
        times++
        runOnUiThread {
            tv_fps.text = "fps: $fps"
        }
    }

    companion object {
        const val TAG = "CameraActivity"
    }
}

class MMessage(val msg: String, val context: Context)