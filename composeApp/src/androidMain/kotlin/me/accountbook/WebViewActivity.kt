package me.accountbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import me.accountbook.WebViewActivity.Companion.EXTRA_URL

class WebViewActivity : ComponentActivity() {
    companion object {
        const val EXTRA_URL = "EXTRA_URL" // 用来传递 URL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url: String? = intent.getStringExtra(EXTRA_URL)
        // 设置 Compose 内容
        setContent {
            if (url == null) {
                Text("url error")
            } else
                WebViewScreen(url)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    var progress by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }


    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            progress = newProgress
                            if (newProgress == 100) {
                                loading = false
                            } else {
                                loading = true
                            }
                        }
                    }

                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            }
        )

        if (loading) {
            CircularProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp),
                trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            )
        }
    }
}

