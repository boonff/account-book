package me.accountbook.data.sync.tagbox

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.local.repository.keyValueStore.TableTimestampRepository
import me.accountbook.data.local.repository.appdatabase.TagboxRepository
import me.accountbook.data.sync.domain.SyncStateManagers
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxSyncUpdate : KoinComponent {
    private val tableTimestampRep: TableTimestampRepository by inject()
    private val tagboxRep: TagboxRepository by inject()
    val tableKey = tagboxRep.tableKey
    private val syncStateManager = SyncStateManagers.getSyncStateManager(tagboxRep.tableKey)


    private suspend fun <T> withChange(block: suspend () -> T): T {
        syncStateManager.setSynced(false)
        return block()
    }

    // 插入标签
    suspend fun insert(data: Tagbox): Boolean = withChange {
        withContext(Dispatchers.IO) {
            if (tagboxRep.insert(data)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }


    // 查询未删除的标签
    suspend fun queryUndeleted(): List<Tagbox> {
        return withContext(Dispatchers.IO) {
            tagboxRep.queryUndeleted() ?: run {
                LoggingUtil.logInfo("tagbox 表为空")
                emptyList()
            }
        }
    }

    // 更新标签名称
    suspend fun updateName(name: String, uuid: String): Boolean = withChange {
        withContext(Dispatchers.IO) {
            if (tagboxRep.updateName(name, uuid)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }

    // 更新标签颜色
    suspend fun updateColor(color: Color, uuid: String): Boolean = withChange {
        withContext(Dispatchers.IO) {
            if (tagboxRep.updateColor(color, uuid)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }

    // 更新标签位置
    suspend fun updatePosition(position: Int, uuid: String): Boolean = withChange {
        withContext(Dispatchers.IO) {
            if (tagboxRep.updatePosition(position, uuid)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }

    suspend fun updatePositions(updates: List<Pair<String, Int>>): Boolean = withChange {
        withContext(Dispatchers.IO) {
            val success = updates.all { (uuid, position) ->
                tagboxRep.updatePosition(position, uuid)
            }
            return@withContext if (success) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                true
            } else
                false
        }
    }


    // 执行软删除
    suspend fun softDelete(uuid: String): Boolean = withChange {
        withContext(Dispatchers.IO) {
            if (tagboxRep.softDelete(uuid)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }
}