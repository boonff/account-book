package me.accountbook.data.local.repository.keyValueStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.local.helper.keyValueStore.TableTimestampHelper
import me.accountbook.data.local.model.TableKey
import me.accountbook.database.TableTimestamp
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent

class TableTimestampRepository(private val dbHelper: TableTimestampHelper) : KoinComponent {
    val tableKey = dbHelper.tableKey

    suspend fun isSynced(tableKey: TableKey): Boolean {
        return withContext(Dispatchers.IO) {
            val item = queryByName(tableKey) ?: run {
                LoggingUtil.logDebug("查询时未发现字段$tableKey")
                return@withContext false
            }
            val netTimestamp = item.netTimestamp ?: run {
                LoggingUtil.logDebug("字段 $tableKey 的 netTimestamp 值为 null")
                return@withContext false
            }
            val localTimestamp = item.localTimestamp ?: run {
                LoggingUtil.logDebug("字段 $tableKey 的 localTimestamp 值为 null")
                return@withContext false
            }
            return@withContext netTimestamp == localTimestamp
        }
    }
    private suspend fun queryByName(tableKey: TableKey): TableTimestamp? {
        return withContext(Dispatchers.IO) {
            dbHelper.queryByName(tableKey.toString())
        }
    }

    private suspend fun queryOrInsert(tableKey: TableKey): TableTimestamp? {
        return withContext(Dispatchers.IO) {
            queryByName(tableKey) ?: run {
                initItem(tableKey)
                queryByName(tableKey)
            }
        }
    }

    private suspend fun initItem(tableKey: TableKey): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.insert(TableTimestamp(tableKey.toString(), null, null))
        }
    }

    suspend fun queryLocalTimestamp(tableKey: TableKey): Long? {
        return withContext(Dispatchers.IO) {
            queryOrInsert(tableKey)?.localTimestamp
        }
    }

    suspend fun queryNetTimestamp(tableKey: TableKey): Long? {
        return withContext(Dispatchers.IO) {
            queryOrInsert(tableKey)?.netTimestamp
        }
    }

    suspend fun updateLocalTimestamp(timestamp: Long, tableKey: TableKey): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(tableKey) ?: return@withContext false
            dbHelper.update(oldItem.copy(localTimestamp = timestamp))
        }
    }

    suspend fun updateNetTimestamp(timestamp: Long, tableKey: TableKey): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(tableKey) ?: return@withContext false
            dbHelper.update(oldItem.copy(netTimestamp = timestamp))
        }
    }

    suspend fun updateTimestamp(timestamp: Long, tableKey: TableKey): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(tableKey) ?: return@withContext false
            dbHelper.update(oldItem.copy(netTimestamp = timestamp, localTimestamp = timestamp))
        }
    }
}
