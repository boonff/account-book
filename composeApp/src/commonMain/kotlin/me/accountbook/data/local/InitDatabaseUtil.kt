package me.accountbook.data.local

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.Database
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class InitDatabaseUtil(driver: SqlDriver) {
    private val queries = Database(driver).appDatabaseQueries

    private fun dropTable() {
        queries.dropAccount()
        queries.dropTagbox()
    }

    private fun dropTrigger() {
        queries.dropTagboxTimstampTragger()
        queries.dropSetPositionTrigger()

        queries.dropSetAccountPositionTrigger()
        queries.dropSetAccountPositionTrigger()
    }

    private fun initTable() {
        queries.createTagbox()
        queries.createAccount()
    }

    private fun initTrigger() {
        queries.createTagboxTimstampTragger()
        queries.createSetTagboxPositionTrigger()

        queries.createAccountTimestampTrigger()
        queries.createSetAccountPositionTrigger()
    }

    fun initDatabase(): Boolean {
        return try {
            queries.transaction {
                //dropTable()
                dropTrigger()
                initTable()
                initTrigger()
            }
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("数据库创建失败", e)
            false
        }

    }
}