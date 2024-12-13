package me.accountbook.data.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import me.accountbook.data.model.NetModel
import me.accountbook.data.model.SerDataItem
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.repository.DatabaseRepository
import me.accountbook.network.UserService
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

abstract class SyncHandler<T : SerDataItem> : KoinComponent {
    protected val userService: UserService by inject()
    var isSynced by mutableStateOf(false)
        protected set

    fun noSynced() {
        isSynced = false
    }

    @OptIn(ExperimentalSerializationApi::class)
    protected inline fun <reified T : SerDataItem> createNetTableModel(data: List<T>): ByteArray {
        return ProtoBuf.encodeToByteArray(NetModel(data, Instant.now().epochSecond))
    }

    protected suspend fun fetchFile(repoFileName: String): ByteArray {
        return userService.fetchFile(repoFileName) ?: run {
            LoggingUtil.logError("同步数据库文件下传失败。")
            return ByteArray(0)
        }
    }

    //合并github仓库中的数据库和本地数据库
    protected fun mergeItems(netItems: List<T>, localItems: List<T>, timestamp: Long?): List<T> {
        //排除已被硬删除的属性（可能会造成数据丢失）
        val clearedItems = localItems.toMutableStateList()
        clearedItems.removeIf {
            (it.timestamp ?: 0) < (timestamp ?: 0L)
        }

        val mergedList = (netItems + clearedItems).sortedBy { it.uuid } // 先按 uuid 排序

        val result = mutableListOf<T>()
        var preItem: T? = null

        // 遍历排序后的列表，选择时间戳更大的数据
        for (item in mergedList) {
            if (preItem == null || preItem.uuid != item.uuid) {
                result.add(item)
                preItem = item
            } else {
                // 如果当前元素的 uuid 与上一个元素相同，保留时间戳最新的
                if ((preItem.timestamp ?: 0) < (item.timestamp ?: 0)) {
                    // 替换旧的 item 为新的时间戳较大的 item
                    result[result.size - 1] = item
                    preItem = item
                }
            }
        }
        return result
    }
}
