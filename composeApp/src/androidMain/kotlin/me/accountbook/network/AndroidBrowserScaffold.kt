package me.accountbook.network

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.accountbook.WebViewActivity

class AndroidBrowserScaffold(private val context: Context) : BrowserScaffold {
    override fun openBrowser(url: String) {
        // 启动 WebViewActivity 显示网页
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, url)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
