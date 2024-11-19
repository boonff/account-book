package me.accountbook.sqldelight

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.database.Account
import me.accountbook.database.Database
import me.accountbook.database.Tagbox

class DatabaseHelper(private val driver: SqlDriver) {

    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    // ---- User 表操作 ----

    // 插入用户
    fun close() {
        driver.close()
    }
    // 插入标签
    suspend fun insertTagBox(name: String, color: Long) {
        withContext(Dispatchers.IO) {
            database.transaction {
                try {
                    queries.insertTagbox(name, color)
                } catch (e: Exception) {
                    println("data insert error: ${e.message}")
                    throw e
                }
            }
        }
    }


    suspend fun queryAllTagBox(): List<Tagbox> {
        return queries.queryAllTagbox().executeAsList()
    }
    suspend fun deleteAllTagBox(){
        withContext(Dispatchers.IO){
            queries.deleteAllTagBox()
        }
    }

    suspend fun queryAllAccount(): List<Account> {
        return queries.queryAllAccount().executeAsList()
    }

}
