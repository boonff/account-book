package me.accountbook

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.data.manager.domain.TagboxManagerImpl
import me.accountbook.data.manager.sync.SyncStateManagers
import me.accountbook.data.manager.sync.TagboxSyncManager
import me.accountbook.data.model.TableKey
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.network.manager.UserManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppInitializer : KoinComponent {
    private val initDB: InitDatabaseUtil by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    private val userManager: UserManager by inject()
    private val tagboxManager: TagboxManagerImpl by inject()
    private val tableTimestampRepository: TableTimestampRepository by inject()
    private val appScope = CoroutineScope(SupervisorJob())

    fun initialize() {
        appScope.launch(Dispatchers.IO) {
            initDB.initDatabase()
            tagboxManager.fetchUnDeleted()
            tagboxManager.sortedTagboxByPosition()

            userManager.initUser {
                tagboxSyncManager.refreshTimestamp()
            }
        }
        appScope.launch(Dispatchers.Default) {
            TableKey.entries.forEach { tableKey ->
                val syncStateManager = SyncStateManagers.getSyncStateManager(tableKey)
                syncStateManager.setSynced(tableTimestampRepository.isSynced(tableKey))
            }
        }
    }
}