package me.accountbook.platform

interface FileStorage {
    fun saveJsonToFile(fileName: String, json: String): Boolean
    fun readJsonFromFile(fileName: String): String?
}

