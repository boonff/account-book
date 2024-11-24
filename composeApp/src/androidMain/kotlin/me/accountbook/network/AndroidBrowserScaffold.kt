package me.accountbook.network

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidBrowserScaffold(private val context: Context) : BrowserScaffold {
    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)  // 使用 context 来启动
    }
}