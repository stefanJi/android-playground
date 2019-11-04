package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.util.Log
import dalvik.system.PathClassLoader
import java.io.File
import java.io.FileOutputStream

/**
 * Create by jy on 2019-10-24
 */
class DexLoaderActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        printCurrentClassClassLoaders()
        printThreadContextClassLoaders()
        loaderOtherApk()
    }

    private fun printCurrentClassClassLoaders() {
        Log.d(TAG, "printCurrentClassClassLoaders")
        var loader = DexLoaderActivity::class.java.classLoader
        while (loader != null) {
            Log.d(TAG, loader.toString())
            loader = loader.parent
        }
    }

    private fun printThreadContextClassLoaders() {
        Log.d(TAG, "printThreadContextClassLoaders")
        val loader = Thread.currentThread().contextClassLoader
        Log.d(TAG, "$loader")
    }

    private fun loaderOtherApk() {
        Log.d(TAG, "loadOtherApk")
        val dexName = "flutter4gitlab.dex"
        val file = File(application.filesDir, dexName)
        if (!file.exists()) {
            val ins = assets.open(dexName)
            val bytes = ByteArray(ins.available())
            ins.read(bytes)
            ins.close()
            val ops = FileOutputStream(file)
            ops.write(bytes)
            ops.flush()
            ops.close()
        }
        val pathDexLoader = MClassLoader(file.absolutePath, null, classLoader)
        val clazz = pathDexLoader.loadClass("io.github.stefanji.fluttergitlab.MainActivity")
        Log.d(TAG, "Other Apk Loaded")
        Log.d(TAG, clazz.toString())
        Log.d(TAG, clazz.classLoader?.toString())
    }

    companion object {
        private const val TAG = "DexLoader"
    }
}

class MClassLoader(dexPath: String?, librarySearchPath: String?, parent: ClassLoader?) :
    PathClassLoader(dexPath, librarySearchPath, parent) {

    override fun loadClass(name: String?): Class<*> {
        Log.d("MClassLoad", "loadClass $name")
        return super.loadClass(name)
    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        Log.d("MClassLoad", "loadClass $name $resolve")
        return super.loadClass(name, resolve)
    }

    override fun findClass(name: String?): Class<*> {
        Log.d("MClassLoad", "findClass $name")
        return super.findClass(name)
    }
}