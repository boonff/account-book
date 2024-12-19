package me.accountbook.data.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import me.accountbook.data.local.helper.TableTimestampHelper
import me.accountbook.database.TableTimestamp
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent

class TableTimestampRepository(private val dbHelper: TableTimestampHelper) : KoinComponent {
    val tableKey = dbHelper.tableKey

    suspend fun isSynced(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val item = queryByName(name) ?: run {
                LoggingUtil.logDebug("查询时未发现字段$name")
                return@withContext false
            }
            val netTimestamp = item.netTimestamp ?: run {
                LoggingUtil.logDebug("字段 $name 的 netTimestamp 值为 null")
                return@withContext false
            }
            val localTimestamp = item.localTimestamp ?: run {
                LoggingUtil.logDebug("字段 $name 的 localTimestamp 值为 null")
                return@withContext false
            }
            return@withContext netTimestamp == localTimestamp
        }
    }
    private suspend fun queryByName(name: String): TableTimestamp? {
        return withContext(Dispatchers.IO) {
            dbHelper.queryByName(name)
        }
    }

    private suspend fun queryOrInsert(name: String): TableTimestamp? {
        return withContext(Dispatchers.IO) {
            queryByName(name) ?: run {
                initItem(name)
                queryByName(name)
            }
        }
    }

    private suspend fun initItem(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.insert(TableTimestamp(name, null, null))
        }
    }

    suspend fun queryLocalTimestamp(name: String): Long? {
        return withContext(Dispatchers.IO) {
            queryByName(name)?.localTimestamp
        }
    }

    suspend fun queryNetTimestamp(name: String): Long? {
        return withContext(Dispatchers.IO) {
            queryByName(name)?.netTimestamp
        }
    }

    suspend fun updateLocalTimestamp(timestamp: Long, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(name) ?: return@withContext false
            dbHelper.update(oldItem.copy(localTimestamp = timestamp))
        }
    }

    suspend fun updateNetTimestamp(timestamp: Long, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(name) ?: return@withContext false
            dbHelper.update(oldItem.copy(netTimestamp = timestamp))
        }
    }

    suspend fun updateTimestamp(timestamp: Long, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val oldItem = queryOrInsert(name) ?: return@withContext false
            dbHelper.update(oldItem.copy(netTimestamp = timestamp, localTimestamp = timestamp))
        }
    }
}
