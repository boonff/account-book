package me.accountbook.data.manager.sync

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxUpdateManager : KoinComponent {
    private val tableTimestampRep: TableTimestampRepository by inject()
    private val tagboxRep: TagboxRepository by inject()

    // 查询未删除的标签
    suspend fun queryUndeleted(): List<Tagbox> {
        return withContext(Dispatchers.IO) {
            tagboxRep.queryUndeleted() ?: run {
                LoggingUtil.logInfo("tagbox 表为空")
                emptyList()
            }
        }
    }

    // 插入标签
    suspend fun insert(name: String, color: Color): Boolean {
        return withContext(Dispatchers.IO) {
            if (tagboxRep.insert(name, color)) {
                tableTimestampRep.updateLocalTimestamp(
                    TimestampUtil.getTimestamp(),
                    tagboxRep.tableKey
                )
                return@withContext true
            }
            return@withContext false
        }
    }

    // 更新标签名称
    suspend fun updateName(name: String, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
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
    suspend fun updateColor(color: Color, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
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
    suspend fun updatePosition(position: Int, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
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

    // 执行软删除
    suspend fun softDelete(uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
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