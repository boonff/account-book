package me.accountbook.network

import android.content.Context
import android.content.Intent
import me.accountbook.WebViewActivity
import me.accountbook.koin.OAuthConfig
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URI

class AndroidLoginManager(
    override val httpClient: OkHttpClient,
    override val browserScaffold: BrowserScaffold,
    override val oauthConfig: OAuthConfig,
    private val context: Context,
) : LoginManager {
    private val serverManager = AndroidServerManager()

    private fun openBrowser(url: String) {
        // 启动 WebViewActivity 显示网页
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, url)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    override fun openLoginPage() {
        val authorizationUrl = getAuthorizationUrl()
        val uri = URI.create(authorizationUrl)

        serverManager.startServer()//启动回调服务器监听授权码
        openBrowser(uri.toString())
    }

    override suspend fun getAccessToken(): String {

    }

    override fun getAuthorizationUrl(): String {

    }
}