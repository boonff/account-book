package me.accountbook.data.sync.domain

import me.accountbook.data.local.model.TableKey

object SyncStateManagers {
    private val syncStateManagerMap = mutableMapOf<TableKey, SyncStateManager>()

    fun getSyncStateManager(tableKey: TableKey): SyncStateManager {
        return syncStateManagerMap.getOrPut(tableKey) { SyncStateManager() }
    }
}