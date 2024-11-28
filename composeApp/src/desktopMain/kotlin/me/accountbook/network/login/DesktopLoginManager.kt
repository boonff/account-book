package me.accountbook.network.login

import org.koin.core.component.KoinComponent
import java.awt.Desktop
import java.net.URI


class DesktopLoginManager : LoginManagerImpl(), KoinComponent {
    private fun openBrowser(url: String) {
        val uri = URI.create(url)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }

    override fun openLoginPage() {
        val authorizationUrl = getAuthorizationUrl()
        val uri = URI.create(authorizationUrl)

        serverUtil.startServer()//启动回调服务器监听授权码
        openBrowser(uri.toString())
    }

    override suspend fun saveAccessToken() {
        val token = getAccessToken()
        fileStore.saveJsonToFile(tokenPath, token)
    }

    override fun readAccessToken(): String? {
        return fileStore.readJsonFromFile(tokenPath)
    }

}
