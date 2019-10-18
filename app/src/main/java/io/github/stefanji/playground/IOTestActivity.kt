package io.github.stefanji.playground

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue
import com.tencent.mrs.plugin.IDynamicConfig
import kotlinx.android.synthetic.main.activity_io_test.*
import java.io.*

/**
 * Create by jy on 2019-10-18
 * 结合 [com.tencent.matrix.iocanary.IOCanaryPlugin] 测试 [ObjectOutputStream] 的文件读写性能
 */
class IOTestActivity : Activity() {

    private val path = File(Environment.getExternalStorageDirectory().path, "test.obj").absolutePath

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_io_test)

        val builder = Matrix.Builder(this.application) // build matrix
        builder.patchListener(TestPluginListener(this) {
            runOnUiThread {
                tv.text = it.toString()
            }
        }) // add general pluginListener

        val dynamicConfig = DynamicConfigImplDemo() // dynamic config

        // init plugin
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build()
        )
        //add to matrix
        builder.plugin(ioCanaryPlugin)

        //init matrix
        Matrix.init(builder.build())

        // start plugin
        ioCanaryPlugin.start()
    }

    @Suppress("UNUSED_PARAMETER")
    fun startTest(v: View) {
        tv.text = ""
        File(path).apply {
            if (exists()) {
                delete()
            }
        }
        writeObjectToFile(path, TestObject())
    }

    @Suppress("UNUSED_PARAMETER")
    fun startTest2(v: View) {
        tv.text = ""
        File(path).apply {
            if (exists()) {
                delete()
            }
        }
        writeObjectToFileWithByteArray(
            path,
            TestObject()
        )
    }
}

class TestObject : Serializable {
    val d = ByteArray(1024 * 30)
    val s = ArrayList<String>()

    init {
        repeat(10000) {
            s.add("$it")
        }
    }
}

fun writeObjectToFile(path: String, obj: Serializable) {
    val oos = ObjectOutputStream(FileOutputStream(path))
    oos.writeObject(obj)
    oos.flush()
    oos.close()
}

fun writeObjectToFileWithByteArray(path: String, obj: Serializable) {
    val bao = ByteArrayOutputStream()
    val oos = ObjectOutputStream(bao)
    oos.writeObject(obj)
    oos.flush()
    oos.close()

    val fos = FileOutputStream(path)
    bao.writeTo(fos)
    fos.flush()
    fos.close()
}

class TestPluginListener(context: Context, val onReport: (Issue) -> Unit) : DefaultPluginListener(context) {

    override fun onReportIssue(issue: Issue) {
        super.onReportIssue(issue)
        onReport(issue)
    }

}

class DynamicConfigImplDemo : IDynamicConfig {

    val isFPSEnable: Boolean
        get() = true
    val isTraceEnable: Boolean
        get() = true
    val isMatrixEnable: Boolean
        get() = true
    val isDumpHprof: Boolean
        get() = false

    override fun get(key: String, defStr: String): String {
        return defStr
    }

    override fun get(key: String, defInt: Int): Int {
//        if (IDynamicConfig.ExptEnum.clicfg_matrix_io_small_buffer_operator_times.name == key) {
//            return 1
//        }
//
//        if (IDynamicConfig.ExptEnum.clicfg_matrix_io_repeated_read_threshold.name == key) {
//            return 1
//        }
//
//        if (IDynamicConfig.ExptEnum.clicfg_matrix_io_main_thread_enable_threshold.name == key) {
//            return 1
//        }
        return defInt
    }

    override fun get(key: String, defLong: Long): Long {
        return defLong
    }

    override fun get(key: String, defBool: Boolean): Boolean {
        return defBool
    }

    override fun get(key: String, defFloat: Float): Float {
        return defFloat
    }
}