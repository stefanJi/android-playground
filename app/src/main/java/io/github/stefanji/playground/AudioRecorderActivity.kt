package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import com.liulishuo.engzo.lingorecorder.LingoRecorder

/**
 * Create by jy on 2019-11-15
 */
class AudioRecorderActivity : Activity() {

    private lateinit var recorder: LingoRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecorder()
        val button = Button(this).apply {
            text = "Start"
            setOnClickListener {
                if (recorder.isRecording) {
                    recorder.stop()
                    text = "Start"
                } else {
                    recorder.start()
                    text = "Stop"
                }
            }
        }

        addContentView(button, ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
    }

    private fun initRecorder() {
        recorder = LingoRecorder()
        recorder.setOnRecordStopListener { throwable, result ->
            Log.d(TAG, "throwable: $throwable  result: $result")
        }
        recorder.setOnProcessStopListener { throwable, map ->
            Log.d(TAG, "throwable: $throwable  map: $map")
        }
    }

    companion object {
        private const val TAG = "AudioRecorderActivity"
    }
}