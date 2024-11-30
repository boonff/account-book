package me.accountbook.network.login

import android.content.Context
import android.content.Intent
import me.accountbook.WebViewActivity
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.KeystoreUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URI

object AndroidLoginManager : LoginManager(), KoinComponent {
    private val context: Context by inject()
    private const val KEY_ALIAS = "access_token"

    private val keystoreUtil = KeystoreUtil(KEY_ALIAS)
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
        ServerUtil.startServer()
        openBrowser(uri.toString())
    }

    override suspend fun saveAccessToken(): Boolean {
        val accessToken = getAccessToken() ?: return false
        val encryptToken = keystoreUtil.encryptData(accessToken)
        return fileStore.saveJsonToFile(tokenPath, encryptToken)
    }

    override suspend fun deleteAccessToken(): Boolean {
        return fileStore.deleteFile(tokenPath)
    }

    override fun readAccessToken(): String? {
        val encryptToken = fileStore.readJsonFromFile(tokenPath)
        return if (encryptToken == null) null
        else
            keystoreUtil.decryptData(encryptToken)
    }
}