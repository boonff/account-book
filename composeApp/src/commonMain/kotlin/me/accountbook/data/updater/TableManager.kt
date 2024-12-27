package me.accountbook.data.updater

import kotlinx.coroutines.flow.StateFlow

interface TableManager<T> {
    val dataList: StateFlow<List<T>>
    suspend fun fetchUnDeleted()
    suspend fun updatePositions(positions: List<Pair<String, Int>>): Boolean
    suspend fun softDelete(uuid: String): Boolean
}
