package me.accountbook.platform

import java.io.File
import java.io.IOException

// 桌面端实现（示例）
class DesktopFileStorage : FileStorage {
    override fun saveJsonToFile(fileName: String, json: String): Boolean {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            file.writeText(json)
            true
        } catch (e: IOException) {
            println("Error saving file: ${e.message}")
            false
        }
    }

    override fun readJsonFromFile(fileName: String): String? {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            if (file.exists()) file.readText() else null
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            null
        }
    }
}