package me.accountbook.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseHelper(private val driver: SqlDriver) {
    private val triggerHelper = TriggerHelper(driver)
    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    fun initializeDatabase() {
        triggerHelper.initializeTriggers()
    }

    // tagbox表的方法
    suspend fun insertTagBox(name: String, color: Color) {
        try {
            withContext(Dispatchers.IO) {
                database.transaction {
                    queries.insertTagbox(name, color.toArgb().toUInt().toLong(), 0)
                }
            }
        } catch (e: Exception) {
            println("Error inserting tag box: ${e.message}")
            throw e
        }
    }

    suspend fun queryAllTagBox(): List<Tagbox> {
        return try {
            withContext(Dispatchers.IO) {
                queries.queryAllTagbox().executeAsList()
            }
        } catch (e: Exception) {
            println("Error querying all tag boxes: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateTagboxName(name: String, tagboxID: Int) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxName(name, tagboxID.toLong())
            }
        } catch (e: Exception) {
            println("Error updateTagboxName:${e.message}")
        }
    }

    suspend fun updateTagboxColor(color: Color, tagboxID: Int) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxColor(color.toArgb().toUInt().toLong(), tagboxID.toLong())
            }
        } catch (e: Exception) {
            println("Error updateTagboxColor${e.message}")
        }
    }

    suspend fun updateTagboxPosition(position: Int, tagboxID: Int) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxPosition(position.toLong(), tagboxID.toLong())
            }
        } catch (e: Exception) {
            println("Error updateTagboxPosition: ${e.message}")
        }
    }

    suspend fun deleteTagBoxById(tagboxID: Int){
        try{
            withContext(Dispatchers.IO){
                queries.deleteTagBoxByID(tagboxID.toLong())
            }
        }catch (e:Exception){
            println("Error deleteTagBoxById: ${e.message}")
        }
    }

    suspend fun deleteAllTagBox() {
        try {
            withContext(Dispatchers.IO) {
                queries.deleteAllTagBox()
            }
        } catch (e: Exception) {
            println("Error deleting all tag boxes: ${e.message}")
            throw e
        }
    }

    // 账户表的方法
    suspend fun queryAllAccount(): List<Account> {
        return try {
            withContext(Dispatchers.IO) {
                queries.queryAllAccount().executeAsList()
            }
        } catch (e: Exception) {
            println("Error querying all accounts: ${e.message}")
            emptyList() // 返回空列表，避免崩溃
        }
    }
}
