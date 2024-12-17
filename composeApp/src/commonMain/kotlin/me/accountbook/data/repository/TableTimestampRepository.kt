package me.accountbook.data.repository

import me.accountbook.data.local.helper.TableTimestampHelper
import me.accountbook.database.TableTimestamp
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent

class TableTimestampRepository(private val dbHelper: TableTimestampHelper):KoinComponent {
    val tableKey = dbHelper.tableKey
    private fun queryByName(name: String): TableTimestamp? {
        return dbHelper.queryByName(name)
    }

    private fun queryOrInsert(name: String): TableTimestamp? {
        return queryByName(name) ?: run {
            initItem(name)
            queryByName(name)
        }
    }
    private fun initItem(name: String): Boolean {
        return dbHelper.insert(
            TableTimestamp(name, null, null)
        )
    }

    fun queryLocalTimestamp(name: String): Long? {
        return queryByName(name)?.localTimestamp
    }

    fun queryNetTimestamp(name: String): Long? {
        return queryByName(name)?.netTimestamp
    }

    fun isSynced(name: String): Boolean {
        val item = queryByName(name) ?: run {
            LoggingUtil.logDebug("查询时未发现字段$name")
            return false
        }
        val netTimestamp = item.netTimestamp ?: run {
            LoggingUtil.logDebug("字段 $name 的 netTimestamp 值为 null")
            return false
        }
        val localTimestamp = item.localTimestamp ?: run {
            LoggingUtil.logDebug("字段 $name 的 localTimestamp 值为 null")
            return false
        }
        return netTimestamp == localTimestamp

    }

    fun updateLocalTimestamp(timestamp: Long, name: String): Boolean {
        val oldItem = queryOrInsert(name) ?: return false
        return dbHelper.update(oldItem.copy(localTimestamp = timestamp))


    }

    fun updateNetTimestamp(timestamp: Long, name: String): Boolean {
        val oldItem = queryOrInsert(name) ?: return false
        return dbHelper.update(oldItem.copy(netTimestamp = timestamp))
    }

    fun updateTimestamp(timestamp: Long, name: String): Boolean {
        val oldItem = queryOrInsert(name) ?: return false
        return dbHelper.update(oldItem.copy(netTimestamp = timestamp, localTimestamp = timestamp))
    }
}