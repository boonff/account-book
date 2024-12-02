package me.accountbook.network

import me.accountbook.network.utils.ServerUtil
import org.koin.core.component.KoinComponent
import java.awt.Desktop
import java.net.URI


object DesktopLoginService : LoginService(), KoinComponent {
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



}
