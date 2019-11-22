package io.github.stefanji.playground

import android.app.Activity
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import java.util.concurrent.LinkedBlockingQueue

/**
 * Create by jy on 2019-11-15
 */
class AudioRecorderActivity : Activity() {

    private lateinit var recorder: AudioRecord
    @Volatile
    private var running = false
    private lateinit var readThread: Thread
    private var bufferSize = 0
    private val queue = LinkedBlockingQueue<ByteArray>()
    private lateinit var playThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecorder()
        val button = Button(this).apply {
            text = "Start"
            setOnClickListener {
                if (running) {
                    Log.d(TAG, "=========stop=========")
                    recorder.stop()
                    readThread.interrupt()
                    playThread.interrupt()
                    running = false
                } else {
                    Log.d(TAG, "=========start=========")
                    running = true
                    text = "Stop"
                    recorder.startRecording()
                    readThread = ReadThread(queue, recorder, bufferSize)
                    readThread.start()

                    playThread = PlayThread(queue)
                    playThread.start()
                }
            }
        }

        addContentView(button, ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
    }

    private fun initRecorder() {
        val sampleRate = 32000
        val channel = AudioFormat.CHANNEL_IN_MONO
        val format = AudioFormat.ENCODING_PCM_8BIT

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, format)
        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            sampleRate,
            channel,
            format,
            bufferSize
        )
    }

    companion object {
        private const val TAG = "AudioRecorderActivity"
    }
}

class ReadThread(
    private val queue: LinkedBlockingQueue<ByteArray>,
    private val recorder: AudioRecord,
    private val bufferSize: Int
) : Thread() {

    override fun run() {
        super.run()
        recorder.startRecording()
        while (!interrupted()) {
            val buffer = ByteArray(bufferSize)
            val read = recorder.read(buffer, 0, bufferSize)
            Log.d("ReadThread", "read: $read")
            if (read > 0) {
                queue.offer(buffer)
            }
        }
    }
}

class PlayThread(
    private val queue: LinkedBlockingQueue<ByteArray>
) : Thread() {
    override fun run() {
        super.run()
        while (!interrupted()) {
            try {
                val data = queue.take()
                Log.d("PlayThread", "play: ${data.size}")
            } catch (e: InterruptedException) {

            }
        }
    }
}