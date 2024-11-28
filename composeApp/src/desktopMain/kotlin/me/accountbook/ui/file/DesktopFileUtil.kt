package me.accountbook.ui.file

import me.accountbook.utils.file.FileUtil
import java.io.File
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

// 桌面端实现（示例）
class DesktopFileUtil : FileUtil {
    override fun isFileExist(fileName: String): Boolean {
        val file = File(fileName)
        return file.exists() // 返回文件是否存在
    }

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

    override fun deleteFile(fileName: String): Boolean {
        return try {
            val file = File(System.getProperty("user.dir"), fileName)
            if (file.exists()) file.delete() else return true
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            return false
        }
    }
}