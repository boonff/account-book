package me.accountbook.network

import java.awt.Desktop
import java.io.File
import java.io.FileWriter
import java.net.URI

class DeskTopNetworkScaffold:NetworkScaffold {
    override fun openBrowser(url: String) {
        val uri = URI.create(url)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }

    override fun saveAccessToken(token: String) {
        val file = File("access_token.txt")
        val writer = FileWriter(file)
        writer.write(token)
        writer.close()
    }

    override fun getAccessToken(): String {
        val file = File("access_token.txt")
        if (file.exists()) {
            return file.readText().trim()
        }
        throw Exception("access_token is null")
    }
}