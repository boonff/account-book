package me.accountbook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.koin.core.component.KoinComponent

class WebViewManager(private val context: Context) : KoinComponent {
    var activity: Activity? = null
    @SuppressLint("SetJavaScriptEnabled")
    fun createWebView(onProgressChanged: (Int) -> Unit): WebView {
        val webView = WebView(context)
        // 启用 JavaScript 和存储
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = false
            cacheMode = WebSettings.LOAD_NO_CACHE  // 不使用缓存
            databaseEnabled = false  // 禁用数据库存储
        }

        // 清理 Cookie
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        webView.webChromeClient = getWebChromeClient(onProgressChanged)
        webView.webViewClient = WebViewClient()


        return webView
    }
    private fun getWebChromeClient(onProgressChanged: (Int) -> Unit): android.webkit.WebChromeClient {
        return object : android.webkit.WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                 super.onProgressChanged(view, newProgress)
                onProgressChanged(newProgress)
            }
        }
    }

    // 关闭 WebViewActivity
    fun closeWebViewActivity() {
        activity?.finish()  // 通过传入的 Activity 调用 finish() 关闭当前 Activity
    }

}
