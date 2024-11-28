package me.accountbook.utils

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.GitHubApiService
import me.accountbook.utils.serialization.CodecUtil
import me.accountbook.utils.serialization.DBItem
import me.accountbook.utils.serialization.SerializableDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

object SyncUtil : KoinComponent {
    private val dbHelper: DatabaseHelper by inject()
    private val githubApi = GitHubApiService

    @Volatile
    var synced: Boolean = false
        private set

    fun setNoSynced() {
        synced = false
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun sync(): Boolean {
        val byteArray = githubApi.fetchFileContentAsByteArray() ?: ByteArray(0)
        val netDB = ProtoBuf.decodeFromByteArray<SerializableDatabase>(byteArray)
        val localDB = CodecUtil.serializationDatabase()

        val tagboxs =
            mergeItems(
                netDB.serializableTagboxs,
                localDB.serializableTagboxs,
                netDB.timestamp
            )

        val mergedDB = SerializableDatabase(tagboxs, Instant.now().epochSecond)

        dbHelper.refactorDatabase(mergedDB)
        synced = githubApi.uploadProtoBufToRepo(mergedDB)
        return true
    }

    suspend fun hardDelete() {
        if (!synced) sync()

        dbHelper.deleteAllDeletedTagbox()
        val localDB = CodecUtil.serializationDatabase()
        localDB.timestamp = Instant.now().epochSecond

        synced = githubApi.uploadProtoBufToRepo(localDB)

    }

    //合并github仓库中的数据库和本地数据库
    private fun <T : DBItem> mergeItems(
        netItems: List<T>,
        localItems: List<T>,
        timestamp: Long?
    ): List<T> {
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