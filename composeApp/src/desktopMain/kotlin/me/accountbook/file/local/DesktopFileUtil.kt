package me.accountbook.file.local

import java.io.File
import java.io.IOException

// 桌面端实现（示例）
class DesktopFileUtil : FileUtil {
    override fun exist(fileName: String): Boolean {
        val file = File(fileName)
        return file.exists() // 返回文件是否存在
    }

    override fun save(fileName: String, content: String): Boolean {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            file.writeText(content)
            true
        } catch (e: IOException) {
            println("Error saving file: ${e.message}")
            false
        }
    }

    override fun read(fileName: String): String? {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            if (file.exists()) file.readText() else null
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            null
        }
    }

    override fun delete(fileName: String): Boolean {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            if (file.exists()) file.delete() else return true
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            return false
        }
    }
}