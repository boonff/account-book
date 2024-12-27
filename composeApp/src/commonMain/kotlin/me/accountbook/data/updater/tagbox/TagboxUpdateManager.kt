package me.accountbook.data.updater.tagbox

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.sync.tagbox.TagboxSyncUpdate
import me.accountbook.data.updater.TableManager
import me.accountbook.database.Tagbox
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

/*
Todo: 更名为tableUpdaterManager,并移动到data.local.manager.tagbox包
Todo: 移除同步状态相关逻辑，转移到tagboxSyncUpdater中
 */

class TagboxUpdateManager : TableManager<Tagbox>, KoinComponent {
    private val tagboxSyncUpdate: TagboxSyncUpdate by inject()
    private val tagboxCatchUpdater: TagboxCatchUpdater by inject()

    override val dataList = tagboxCatchUpdater.catchList

    // 查询未删除的标签
    override suspend fun fetchUnDeleted() {
        tagboxCatchUpdater.updateList(tagboxSyncUpdate.queryUndeleted())
    }

    fun sortedListByPosition() {
        tagboxCatchUpdater.sortedByPosition()
    }

    fun sortedListByTimestamp() {
        tagboxCatchUpdater.sortedByTimestamp()
    }

    // 插入标签
    suspend fun insert(name: String, color: Color): Boolean = withContext(Dispatchers.IO) {
        val data = Tagbox(
            uuid = UUID.randomUUID().toString(),
            name = name,
            color = color.toArgb().toUInt().toLong(),
            position = TimestampUtil.getTimestamp(),
            timestamp = TimestampUtil.getTimestamp(),
            deleted = null
        )
        val insertSuccess = tagboxSyncUpdate.insert(data)
        if (insertSuccess) {
            tagboxCatchUpdater.addItem(data)
        }
        insertSuccess
    }

    // 更新标签名称
    suspend fun updateName(name: String, uuid: String): Boolean = withContext(Dispatchers.IO) {
        val updateSuccess = tagboxSyncUpdate.updateName(name, uuid)
        if (updateSuccess) {
            tagboxCatchUpdater.updateName(uuid, name)
        }
        updateSuccess

    }

    // 更新标签颜色
    suspend fun updateColor(color: Color, uuid: String): Boolean = withContext(Dispatchers.IO) {
        val updateSuccess = tagboxSyncUpdate.updateColor(color, uuid)
        if (updateSuccess) {
            tagboxCatchUpdater.updateColor(uuid, color)
        }
        updateSuccess

    }

    override suspend fun updatePositions(positions: List<Pair<String, Int>>): Boolean =
        withContext(Dispatchers.Default) {
            tagboxSyncUpdate.updatePositions(positions)
        }


    // 执行软删除
    override suspend fun softDelete(uuid: String): Boolean = withContext(Dispatchers.IO) {
        val deleteSuccess = tagboxSyncUpdate.softDelete(uuid)
        if (deleteSuccess) {
            tagboxCatchUpdater.removeItem(uuid)
        }
        deleteSuccess
    }
}