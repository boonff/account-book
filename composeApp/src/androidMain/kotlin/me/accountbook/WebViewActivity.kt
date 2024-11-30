package me.accountbook

import android.content.Intent
import android.os.Bundle
import android.view.ViewManager
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

class WebViewActivity : ComponentActivity() {
    val webViewManager: WebViewManager by inject()

    companion object {
        const val EXTRA_URL = "EXTRA_URL" // 用来传递 URL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url: String? = intent.getStringExtra(EXTRA_URL)
        webViewManager.activity = this
        // 设置 Compose 内容
        setContent {
            if (url == null) {
                Text("url error")
            } else {
                WebViewScreen(url)
            }
        }
    }
}

@Composable
fun WebViewScreen(url: String) {
    val webViewManager: WebViewManager = koinInject()

    var progress by remember { mutableIntStateOf(0) } // 加载进度
    var loading by remember { mutableStateOf(true) } // 是否加载中

    Column(modifier = Modifier.fillMaxSize()) {
        // 如果正在加载，显示顶部 LinearProgressIndicator
        if (loading) {
            LinearProgressIndicator(
                progress = {
                    progress / 100f // 显示进度条
                },
                modifier = Modifier
                    .fillMaxWidth()  // 占满整个宽度
                    .height(4.dp), // 设置高度
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f), // 设置进度条的背景色
            )
        }
        // 创建 WebView，并且绑定进度更新
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            factory = {
                val webView = webViewManager.createWebView { newProgress ->
                    progress = newProgress
                    loading = newProgress < 100 // 如果进度 < 100，则继续加载
                }
                webView.loadUrl(url)
                webView
            }
        )

    }
}

