package io.github.stefanji.webview

import android.content.Context
import android.webkit.WebView

class MWebView(val context: Context) {
    private val mWebView: WebView

    init {
        mWebView = WebView(context)
        mWebView.settings.apply {
            javaScriptEnabled = true
        }
        mWebView.addJavascriptInterface(JsBridge(), "StefanJiJsBridge")
    }
}