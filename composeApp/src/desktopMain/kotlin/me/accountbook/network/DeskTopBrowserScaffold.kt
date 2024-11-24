package me.accountbook.network

import java.awt.Desktop
import java.net.URI

class DeskTopBrowserScaffold:BrowserScaffold {
    override fun openBrowser(url: String) {
        val uri = URI.create(url)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }
}