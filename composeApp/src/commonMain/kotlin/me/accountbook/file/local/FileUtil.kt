package me.accountbook.file.local

interface FileUtil {
    fun exist(fileName: String): Boolean
    fun save(fileName: String, content: String): Boolean
    fun read(fileName: String): String?
    fun delete(fileName: String): Boolean
}

