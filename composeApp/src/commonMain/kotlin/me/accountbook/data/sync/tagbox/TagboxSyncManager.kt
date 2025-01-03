package me.accountbook.data.sync.tagbox

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.local.model.SerTagbox
import me.accountbook.data.local.model.encode
import me.accountbook.data.local.repository.keyValueStore.TableTimestampRepository
import me.accountbook.data.local.repository.appdatabase.TagboxRepository
import me.accountbook.data.sync.domain.SyncLogic
import me.accountbook.data.sync.domain.SyncStateManagers
import me.accountbook.network.manager.SyncUtil
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxSyncManager : KoinComponent {
    private val tableTimestampRep: TableTimestampRepository by inject()
    private val tagboxRep: TagboxRepository by inject()
    private val tableKey = tagboxRep.tableKey
    private val syncStateManager get() = SyncStateManagers.getSyncStateManager(tableKey)

    suspend fun sync(): Boolean {
        syncStateManager.startLoading()
        val localTable = tagboxRep.queryUndeleted()?.map { it.encode() } ?: emptyList()
        val netTable = SyncUtil.fetchTable<SerTagbox>(tableKey) ?: emptyList()
        val netTimestamp = SyncUtil.fetchTimestamp(tableKey)
        val nowTimestamp = TimestampUtil.getTimestamp()

        tableTimestampRep.updateTimestamp(nowTimestamp, tableKey)
        val merged = SyncLogic.mergeList(netTable, localTable, netTimestamp)
        tagboxRep.refactor(merged.map { it.decode() })

        val uploadSuccess = SyncUtil.uploadTable(tableKey, merged)
                && SyncUtil.uploadTimestamp(nowTimestamp, tableKey)
        syncStateManager.endLoading()
        syncStateManager.setSynced(tableTimestampRep.isSynced(tableKey))
        return uploadSuccess
    }

    suspend fun refreshTimestamp(): Boolean {
        return withContext(Dispatchers.IO) {
            val netTimestamp =
                SyncUtil.fetchTimestamp(tagboxRep.tableKey) ?: return@withContext false
            tableTimestampRep.updateNetTimestamp(netTimestamp, tagboxRep.tableKey)
        }
    }

    // 检查是否已同步
    private suspend fun isSynced(): Boolean {
        return withContext(Dispatchers.IO) {
            tableTimestampRep.isSynced(tagboxRep.tableKey)
        }
    }
}
