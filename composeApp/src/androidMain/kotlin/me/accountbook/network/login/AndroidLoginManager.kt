package me.accountbook.network.login

import android.content.Context
import android.content.Intent
import me.accountbook.WebViewActivity
import me.accountbook.utils.file.KeystoreUtil
import org.koin.core.component.KoinComponent
import java.net.URI

class AndroidLoginManager(
    private val context: Context,
) : LoginManagerImpl(), KoinComponent {

    private val keystoreUtil = KeystoreUtil("access_token")
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

        serverUtil.startServer()//启动回调服务器监听授权码
        openBrowser(uri.toString())
    }

    override suspend fun saveAccessToken() {
        val encryptToken = keystoreUtil.encryptData(getAccessToken())
        fileStore.saveJsonToFile("token", encryptToken) //文件名需要解耦
    }

    override fun readAccessToken(): String? {
        val encryptToken = fileStore.readJsonFromFile("token")
        return if (encryptToken == null) null
        else
            keystoreUtil.decryptData(encryptToken)
    }
}