package me.accountbook.data.local.helper

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.Database
import me.accountbook.database.Setting
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class SettingHelper(driver: SqlDriver) {
    val tableKey = "Setting"
    private val queries = Database(driver).keyValueStoreQueries
    fun insert(data: Setting): Boolean {
        return try {
            queries.insertSetting(data.key, data.value_)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("设置插入失败", e)
            false
        }
    }

    fun querySettingByKey(key: String): Setting? {
        return try {
            queries.querySettingByKey(key).executeAsOneOrNull()
        } catch (e: SQLException) {
            LoggingUtil.logError("设置\"$key\"获取失败", e)
            null
        }
    }
}