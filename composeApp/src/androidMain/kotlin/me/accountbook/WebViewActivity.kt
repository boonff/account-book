package me.accountbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this).apply {
            settings.javaScriptEnabled = true // 允许 JavaScript
            settings.domStorageEnabled = true // 启用 DOM 存储
            webChromeClient = WebChromeClient() // 用于显示加载进度
            webViewClient = WebViewClient() // 用于处理网页内的链接点击
        }

        setContentView(webView)

        // 获取传递的 URL 并加载
        val url = intent.getStringExtra(EXTRA_URL) ?: return
        webView.loadUrl(url)
    }

    companion object {
        const val EXTRA_URL = "EXTRA_URL" // 用来传递 URL
    }
}
