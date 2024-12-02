package me.accountbook.utils.file

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException

class AndroidFileUtil(private val context: Context) : FileUtil {
    override fun exist(fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists() // 返回文件是否存在
    }

    override fun save(fileName: String, content: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.writeText(content)
            true
        } catch (e: IOException) {
            Log.e("AndroidFileStorage", "Error saving file", e)
            false
        }
    }

    override fun read(fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) file.readText() else null
        } catch (e: IOException) {
            Log.e("AndroidFileStorage", "Error reading file", e)
            null
        }
    }

    override fun delete(fileName: String):Boolean {
        return try{
            val file = File(context.filesDir, fileName)
            if (file.exists()) file.delete() else return true
        }catch (e:Exception){
            Log.e("AndroidFileStorage", "Error reading file", e)
            false
        }
    }
}




