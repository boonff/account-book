package me.accountbook.data.local

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.Database
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class InitDatabaseUtil(driver: SqlDriver) {
    private val appDatabaseQueries = Database(driver).appDatabaseQueries
    private val keyValueStoreQueries = Database(driver).keyValueStoreQueries

    private fun dropTable() {
        appDatabaseQueries.dropAccount()
        appDatabaseQueries.dropTagbox()
        keyValueStoreQueries.dropSetting()
        keyValueStoreQueries.dropTableTamestamp()
    }

    private fun dropTrigger() {
        appDatabaseQueries.dropTagboxTimstampTragger()
        appDatabaseQueries.dropSetPositionTrigger()

        appDatabaseQueries.dropSetAccountPositionTrigger()
        appDatabaseQueries.dropSetAccountPositionTrigger()
    }

    private fun initTable() {
        appDatabaseQueries.createTagbox()
        appDatabaseQueries.createAccount()

        keyValueStoreQueries.createSetting()
        keyValueStoreQueries.createTableTamestamp()
    }

    private fun initTrigger() {
        appDatabaseQueries.createTagboxTimstampTragger()
        //appDatabaseQueries.createSetTagboxPositionTrigger()

        appDatabaseQueries.createAccountTimestampTrigger()
        //appDatabaseQueries.createSetAccountPositionTrigger()
    }

    fun initDatabase(): Boolean {
        return try {
            appDatabaseQueries.transaction {
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