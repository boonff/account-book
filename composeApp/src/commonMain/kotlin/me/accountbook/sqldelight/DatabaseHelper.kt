package me.accountbook.sqldelight

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.database.Account
import me.accountbook.database.Database
import me.accountbook.database.Tagbox

class DatabaseHelper(private val driver: SqlDriver) {
    private val triggerHelper = TriggerHelper(driver)
    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    fun initializeDatabase() {
        triggerHelper.initializeTriggers()
    }

    // 插入标签
    suspend fun insertTagBox(name: String, color: Long) {
        try {
            database.transaction {
                queries.insertTagbox(name, color, 0)

            }
        } catch (e: Exception) {
            println("Error inserting tag box: ${e.message}")
            throw e  // 重新抛出异常，确保调用者能够捕获
        }
    }

    // 查询所有标签
    suspend fun queryAllTagBox(): List<Tagbox> {
        return try {
            queries.queryAllTagbox().executeAsList()

        } catch (e: Exception) {
            println("Error querying all tag boxes: ${e.message}")
            emptyList() // 返回空列表，避免崩溃
        }
    }

    // 删除所有标签
    suspend fun deleteAllTagBox() {
        try {
            queries.deleteAllTagBox()

        } catch (e: Exception) {
            println("Error deleting all tag boxes: ${e.message}")
            throw e  // 重新抛出异常，确保调用者能够捕获
        }
    }

    suspend fun updateTagboxPosition(position: Int, tagboxID: Long) {
        try {
            queries.updateTagboxPosition(position.toLong(), tagboxID)

        } catch (e: Exception) {
            println("Error updateTagboxPosition: ${e.message}")
        }
    }

    // 查询所有账户
    suspend fun queryAllAccount(): List<Account> {
        return try {
            queries.queryAllAccount().executeAsList()

        } catch (e: Exception) {
            println("Error querying all accounts: ${e.message}")
            emptyList() // 返回空列表，避免崩溃
        }
    }
}
