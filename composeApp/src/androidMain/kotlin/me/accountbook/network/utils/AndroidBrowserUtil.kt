package me.accountbook.network.utils

import android.content.Context
import android.content.Intent
import me.accountbook.WebViewActivity

class AndroidBrowserUtil(private val context: Context) : BrowserUtil {
    override fun openBrowser(url: String) {
        // 启动 WebViewActivity 显示网页
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, url)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
