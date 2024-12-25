package me.accountbook.data.manager.sync

import me.accountbook.data.model.TableKey

object SyncStateManagers {
    private val syncStateManagerMap = mutableMapOf<TableKey, SyncStateManager>()

    fun getSyncStateManager(tableKey: TableKey): SyncStateManager {
        return syncStateManagerMap.getOrPut(tableKey) { SyncStateManager() }
    }
}