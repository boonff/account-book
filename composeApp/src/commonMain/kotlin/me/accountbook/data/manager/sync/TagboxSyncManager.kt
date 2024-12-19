package me.accountbook.data.manager.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.model.SerDataItem
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.model.encode
import me.accountbook.data.repository.AppDatabaseRepository
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.network.manager.SyncUtil
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxSyncManager : KoinComponent {
    private val tableTimestampRep: TableTimestampRepository by inject()
    private val tagboxRep: TagboxRepository by inject()
    private val tableKey = tagboxRep.tableKey

    suspend fun sync(): Boolean {
        val localTable = tagboxRep.queryUndeleted()?.map { it.encode() } ?: emptyList()//未声明调试信息
        val netTable = SyncUtil.fetchTable<SerTagbox>(tableKey) ?: emptyList()
        val netTimestamp = SyncUtil.fetchTimestamp(tableKey)
        val nowTimestamp = TimestampUtil.getTimestamp()

        tableTimestampRep.updateTimestamp(nowTimestamp, tableKey)
        val merged = SyncLogic.mergeList(netTable, localTable, netTimestamp)
        tagboxRep.refactor(merged.map { it.decode() })

        return SyncUtil.uploadTable(tableKey, merged) &&
                SyncUtil.uploadTimestamp(nowTimestamp, tableKey)
    }

    suspend fun refreshTimestamp(): Boolean {
        return withContext(Dispatchers.IO) {
            val netTimestamp =
                SyncUtil.fetchTimestamp(tagboxRep.tableKey) ?: return@withContext false
            tableTimestampRep.updateNetTimestamp(netTimestamp, tagboxRep.tableKey)
        }
    }

    // 检查是否已同步
    suspend fun isSynced(): Boolean {
        return withContext(Dispatchers.IO) {
            tableTimestampRep.isSynced(tagboxRep.tableKey)
        }
    }

    // 检查是否未同步
    suspend fun unSynced(): Boolean {
        return withContext(Dispatchers.IO) {
            !isSynced()
        }
    }

}