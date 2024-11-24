package me.accountbook.platform

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException

class AndroidFileStorage(private val context: Context) : FileStorage {
    override fun saveJsonToFile(fileName: String, json: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.writeText(json)
            true
        } catch (e: IOException) {
            Log.e("AndroidFileStorage", "Error saving file", e)
            false
        }
    }

    override fun readJsonFromFile(fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) file.readText() else null
        } catch (e: IOException) {
            Log.e("AndroidFileStorage", "Error reading file", e)
            null
        }
    }
}




