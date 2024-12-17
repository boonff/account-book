package me.accountbook.data.manager.sync

import androidx.compose.runtime.toMutableStateList
import me.accountbook.data.model.SerAccount
import me.accountbook.data.model.SerDataItem
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.model.encode
import me.accountbook.data.repository.AccountRepository
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//需要改为工具类
abstract class AppDataManager : KoinComponent {
    protected val timestampRepository: TableTimestampRepository by inject()

    fun <T : SerDataItem> mergeItems(
        netItems: List<T>,
        localItems: List<T>,
        netTimestamp: Long?
    ): List<T> {
        //排除已被硬删除的属性（可能会造成数据丢失）
        val clearedItems = localItems.toMutableStateList()
        clearedItems.removeIf {
            (it.timestamp ?: 0) < (netTimestamp ?: 0L)
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

object TagboxSyncManager : AppDataManager() {
    private val tagboxRepository: TagboxRepository by inject()
    private val tableKey = tagboxRepository.tableKey


    suspend fun sync(): Boolean {
        val localTable = tagboxRepository.query()?.map { it.encode() } ?: emptyList()//未声明调试信息
        val netTable = SyncUtil.fetchTable<SerTagbox>(tableKey) ?: emptyList()
        val netTimestamp = SyncUtil.fetchTimestamp(tableKey)
        val nowTimestamp = TimestampUtil.getTimestamp()

        timestampRepository.updateTimestamp(nowTimestamp, tableKey)
        val merged = mergeItems(netTable, localTable, netTimestamp)
        tagboxRepository.refactor(merged.map { it.decode() })

        return SyncUtil.uploadTable(tableKey, merged) &&
                SyncUtil.uploadTimestamp(nowTimestamp, tableKey)
    }
}

object AccountSyncManager : AppDataManager() {
    private val accountRepository: AccountRepository by inject()
    private val tableKey = accountRepository.tableKey

    suspend fun sync(): Boolean {
        val localTable = accountRepository.query()?.map { it.encode() } ?: emptyList()
        val netTable = SyncUtil.fetchTable<SerAccount>(tableKey) ?: emptyList()
        val netTimestamp = SyncUtil.fetchTimestamp(tableKey)
        val nowTimestamp = TimestampUtil.getTimestamp()

        timestampRepository.updateTimestamp(nowTimestamp, tableKey)
        val merged = mergeItems(netTable, localTable, netTimestamp)
        accountRepository.refactor(merged.map { it.decode() })

        accountRepository.refactor(merged.map { it.decode() })
        return SyncUtil.uploadTable(tableKey, merged) &&
                SyncUtil.uploadTimestamp(nowTimestamp, tableKey)

    }
}