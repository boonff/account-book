package me.accountbook.data.local.helper.keyValueStore

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.Database
import me.accountbook.database.TableTimestamp
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class TableTimestampHelper(driver: SqlDriver) {
    private val queries = Database(driver).keyValueStoreQueries
    val tableKey = "tableTimestamp"


    fun insert(data: TableTimestamp): Boolean {
        return try {
            queries.insertTableTamestamp(data.name, data.netTimestamp, data.localTimestamp)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("TableTimestamp插入失败", e)
            return false
        }
    }

    fun query(): List<TableTimestamp>? {
        return try {
            queries.queryTablestamp().executeAsList()
        } catch (e: SQLException) {
            LoggingUtil.logError("TableTimestamp列表获取失败", e)
            null
        }
    }

    fun queryByName(name: String): TableTimestamp? {
        return try {
            queries.queryTablestampByName(name).executeAsOneOrNull()
        } catch (e: SQLException) {
            LoggingUtil.logError("TableTimestamp\"$name\"获取失败", e)
            null
        }
    }

    fun update(tableTimestamp: TableTimestamp): Boolean {
        return try {
            queries.updateTableTamestampByName(
                tableTimestamp.netTimestamp,
                tableTimestamp.localTimestamp,
                tableTimestamp.name
            )
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("TableTimestamp\"${tableTimestamp.name}\"更新失败", e)
            false
        }
    }
}