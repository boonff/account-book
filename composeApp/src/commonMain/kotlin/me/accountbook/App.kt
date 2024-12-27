package me.accountbook

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.accountbook.data.local.helper.InitDatabaseUtil
import me.accountbook.data.updater.tagbox.TagboxUpdateManager
import me.accountbook.data.sync.domain.SyncStateManagers
import me.accountbook.data.sync.tagbox.TagboxSyncManager
import me.accountbook.data.local.model.TableKey
import me.accountbook.data.local.repository.keyValueStore.TableTimestampRepository
import me.accountbook.network.manager.UserManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppInitializer : KoinComponent {
    private val initDB: InitDatabaseUtil by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    private val userManager: UserManager by inject()
    private val tagboxManager: TagboxUpdateManager by inject()
    private val tableTimestampRepository: TableTimestampRepository by inject()
    private val appScope = CoroutineScope(SupervisorJob())

    fun initialize() {
        appScope.launch(Dispatchers.IO) {
            initDB.initDatabase()
            tagboxManager.fetchUnDeleted()
            tagboxManager.sortedListByPosition()

            userManager.initUser {
                // 使用 async 启动一个协程，并返回 Deferred 对象
                val refreshTimestampDeferred = async {
                    tagboxSyncManager.refreshTimestamp()
                }
                // 等待 refreshTimestamp 完成
                refreshTimestampDeferred.await()

                // 在 refreshTimestamp 完成后执行第二个协程
                TableKey.entries.forEach { tableKey ->
                    val syncStateManager = SyncStateManagers.getSyncStateManager(tableKey)
                    syncStateManager.setSynced(tableTimestampRepository.isSynced(tableKey))
                }
            }
        }
    }
}