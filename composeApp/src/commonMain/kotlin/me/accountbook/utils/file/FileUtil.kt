package me.accountbook.utils.file

interface FileUtil {
    fun isFileExist(fileName: String): Boolean
    fun saveJsonToFile(fileName: String, json: String): Boolean
    fun readJsonFromFile(fileName: String): String?

    fun deleteFile(fileName: String): Boolean
}

