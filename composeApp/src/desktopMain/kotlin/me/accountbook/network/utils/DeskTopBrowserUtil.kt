package me.accountbook.network.utils

import java.awt.Desktop
import java.net.URI

class DeskTopBrowserUtil: BrowserUtil {
    override fun openBrowser(url: String) {
        val uri = URI.create(url)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }
}