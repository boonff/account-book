package me.accountbook.data.manager.sync

import androidx.compose.runtime.toMutableStateList
import me.accountbook.data.model.SerDataItem
import org.koin.core.component.KoinComponent


object SyncLogic : KoinComponent {
    fun <T : SerDataItem> mergeList(
        netList: List<T>,
        localList: List<T>,
        netTimestamp: Long?,
    ): List<T> {
        //排除已被硬删除的属性（可能会造成数据丢失）
        val clearedItems = localList.toMutableStateList()
//        clearedItems.removeIf {
//            (it.timestamp ?: 0) < (netTimestamp ?: 0L)
//        }

        val mergedList = (netList + clearedItems).sortedBy { it.uuid } // 先按 uuid 排序

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