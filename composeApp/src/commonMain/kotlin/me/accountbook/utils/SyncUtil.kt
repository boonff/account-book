package me.accountbook.utils

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

object SyncUtil : KoinComponent {
    private val dbHelper: DatabaseHelper by inject()
    private val githubApi = GitHubApiService

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun sync(): Boolean {
        val byteArray = githubApi.fetchFileContentAsByteArray() ?: ByteArray(0)
        val netDB = ProtoBuf.decodeFromByteArray<SerializableDatabase>(byteArray)
        val localDB = CodecUtil.serializationDatabase()

        val tagboxs = mergeItems(netDB.serializableTagboxs, localDB.serializableTagboxs)

        val mergedDB = SerializableDatabase(tagboxs)

        updateDatabase(mergedDB, localDB)
        githubApi.uploadProtoBufToRepo(mergedDB)
        return true
    }

    //合并github仓库中的数据库和本地数据库
    private fun <T : DBItem> mergeItems(netItems: List<T>, localItems: List<T>): List<T> {
        // 合并两个列表并按 uuid 排序
        val mergedList = (netItems + localItems).sortedBy { it.uuid } // 先按 uuid 排序

        val result = mutableListOf<T>()
        var previousItem: T? = null

        // 遍历排序后的列表，选择时间戳更大的数据
        for (item in mergedList) {
            if (previousItem == null || previousItem.uuid != item.uuid) {
                // 如果当前元素的 uuid 与上一个元素不一样，添加当前元素
                result.add(item)
                previousItem = item
            } else {
                // 如果当前元素的 uuid 与上一个元素相同，保留时间戳最新的
                if ((previousItem.timestamp ?: 0) < (item.timestamp ?: 0)) {
                    // 替换旧的 item 为新的时间戳较大的 item
                    result[result.size - 1] = item
                    previousItem = item
                }
            }
        }

        return result
    }

    //更新本地数据库
    private suspend fun updateDatabase(
        mergedDB: SerializableDatabase, localDB: SerializableDatabase
    ) {
        mergedDB.serializableTagboxs.forEach { index ->
            if (index.uuid in localDB.serializableTagboxs.map { it.uuid }) dbHelper.updateTagbox(
                index
            )
            else dbHelper.insertTagBox(index)
        }
    }
}