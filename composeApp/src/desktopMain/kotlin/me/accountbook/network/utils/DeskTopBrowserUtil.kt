package me.accountbook.network.utils

import java.awt.Desktop
import java.net.URI

class DeskTopBrowserUtil : BrowserUtil {
    override fun openBrowser(url: String) {
        val timestamp = System.currentTimeMillis()
        val urlWithNoCache = "$url?nocache=$timestamp"
        val uri = URI.create(urlWithNoCache)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }
}