package me.accountbook.data.manager.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import me.accountbook.data.manager.sync.SyncStateManagers
import me.accountbook.data.manager.sync.TagboxUpdateManager
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.database.Tagbox
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

interface TableManager {
    val tagboxList: StateFlow<List<Tagbox>>
    suspend fun fetchUnDeleted()
    suspend fun insert(name: String, color: Color): Boolean
    suspend fun updateName(name: String, uuid: String): Boolean
    suspend fun updateColor(color: Color, uuid: String): Boolean
    suspend fun updatePositions(positions: List<Pair<String, Int>>): Boolean
    suspend fun softDelete(uuid: String): Boolean
    suspend fun updateTagboxList(newList: List<Tagbox>)
}

class TagboxManagerImpl : TableManager, KoinComponent {
    private val tagboxUpdateManager: TagboxUpdateManager by inject()
    private val tagboxListUpdater: TagboxListUpdater by inject()
    private val tagboxRepository: TagboxRepository by inject()
    val syncStateManager = SyncStateManagers.getSyncStateManager(tagboxRepository.tableKey)

    override val tagboxList = tagboxListUpdater.tagboxList
    private suspend fun <T> withChange(block: suspend () -> T): T {
        syncStateManager.startLoading()
        val result = block()
        syncStateManager.endLoading()
        syncStateManager.setSynced(false)
        return result
    }

    // 查询未删除的标签
    override suspend fun fetchUnDeleted() {
        tagboxListUpdater.updateList(tagboxUpdateManager.queryUndeleted())
    }
    fun sortedTagboxByPosition(){
        tagboxListUpdater.sortedByPosition()
    }
    fun sortedTagboxByTimestamp(){
        tagboxListUpdater.sortedByTimestamp()
    }

    // 插入标签
    override suspend fun insert(name: String, color: Color): Boolean = withChange {
        val data = Tagbox(
            uuid = UUID.randomUUID().toString(),
            name = name,
            color = color.toArgb().toUInt().toLong(),
            position = TimestampUtil.getTimestamp(),
            timestamp = TimestampUtil.getTimestamp(),
            deleted = null
        )
        val insertSuccess = tagboxUpdateManager.insert(data)
        if (insertSuccess) {
            tagboxListUpdater.addItem(data)
        }
        insertSuccess
    }

    // 更新标签名称
    override suspend fun updateName(name: String, uuid: String): Boolean =
        withContext(Dispatchers.IO) {
            withChange {
                val updateSuccess = tagboxUpdateManager.updateName(name, uuid)
                if (updateSuccess) {
                    tagboxListUpdater.updateName(uuid, name)
                }

                updateSuccess
            }
        }

    // 更新标签颜色
    override suspend fun updateColor(color: Color, uuid: String): Boolean =
        withContext(Dispatchers.IO) {
            withChange {
                val updateSuccess = tagboxUpdateManager.updateColor(color, uuid)
                if (updateSuccess) {
                    tagboxListUpdater.updateColor(uuid, color)
                }
                updateSuccess
            }
        }

    override suspend fun updatePositions(positions: List<Pair<String, Int>>): Boolean =
        withContext(Dispatchers.Default) {
            withChange {
                tagboxUpdateManager.updatePositions(positions)
            }
        }


    // 执行软删除
    override suspend fun softDelete(uuid: String): Boolean = withContext(Dispatchers.IO) {
        withChange {
            val deleteSuccess = tagboxUpdateManager.softDelete(uuid)
            if (deleteSuccess) {
                tagboxListUpdater.removeItem(uuid)
            }
            deleteSuccess
        }
    }

    override suspend fun updateTagboxList(newList: List<Tagbox>) {
        tagboxListUpdater.updateList(newList)
    }
}