package me.accountbook.network

import android.content.Context
import android.content.Intent
import me.accountbook.WebViewActivity
import me.accountbook.WebViewManager
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.KeystoreUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URI

object AndroidLoginService : LoginService(), KoinComponent {
    private val context: Context by inject()
    private fun openBrowser(url: String) {
        // 启动 WebViewActivity 显示网页
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    override fun openLoginPage() {
        val authorizationUrl = getAuthorizationUrl()
        val uri = URI.create(authorizationUrl)
        ServerUtil.startServer()
        openBrowser(uri.toString())
    }
}