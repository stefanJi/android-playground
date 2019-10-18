package io.github.stefanji.playground

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.filter.NoFilter
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import io.github.stefanji.playground.widget.BeautyFilter
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * Create by jy on 2019-10-10
 */
class CameraActivity : FragmentActivity(), FrameProcessor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
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
}
