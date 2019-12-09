package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.*
import android.widget.FrameLayout


/**
 * Create by jy on 2019-12-09
 */
class JsBridgeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frameLayout = FrameLayout(this)
        setContentView(frameLayout, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        val webView = WebView(this)

        frameLayout.addView(webView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        webView.addJavascriptInterface(MJsBridge(), "MJsBridge")
        webView.settings.apply {
            javaScriptEnabled = true
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        val html = """
            <html>
                <head>
                    
                </head>
                <body style="background: grey;">
                    <button onclick="onClick()" style="background:red;">Hello</button>
                </body>
                <script>
                    function onClick(){
                        window.MJsBridge.hello();
                        
                        // 通过反射拿到任何一个内部类, 比如 java.lang.Runtime, 然后执行命令.
                        // 只在 Android 4.1 及以下能使用, 4.2 及以上只能调用被 JavascriptInterface 注解了的方法, 而 getClass 等方法
                        // 没有被注解, 也就无法访问, js 会找不到方法, 所以就避免了这个安全漏洞
                        var cls = window.MJsBridge.getClass();
                        var method = cls.getMethod("other", null);
                        method.invoke(window.MJsBridge);
                    }
                </script>
            </html>
        """.trimIndent()
        val encodedHtml: String = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
        webView.loadData(encodedHtml, "text/html", "base64")
    }
}

class MJsBridge {

    @JavascriptInterface
    fun hello(): String {
        Log.d("JY", "[MJsBridge hello]")
        return "hello from Android ${System.currentTimeMillis()}"
    }

}