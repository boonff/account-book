package me.accountbook.network.login

import me.accountbook.network.utils.ServerUtil
import org.koin.core.component.KoinComponent
import java.awt.Desktop
import java.net.URI


object DesktopLoginManager : LoginManager(), KoinComponent {
    private fun openBrowser(url: String) {
        val uri = URI.create(url)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }

    override fun openLoginPage() {
        val authorizationUrl = getAuthorizationUrl()
        val uri = URI.create(authorizationUrl)
        ServerUtil.startServer()
        openBrowser(uri.toString())
    }

    override suspend fun saveAccessToken(): Boolean {
        val token = getAccessToken() ?: return false
        return fileStore.saveJsonToFile(tokenPath, token)
    }

    override suspend fun deleteAccessToken(): Boolean {
        return fileStore.deleteFile(tokenPath)
    }

    override fun readAccessToken(): String? {
        return fileStore.readJsonFromFile(tokenPath)
    }

}
