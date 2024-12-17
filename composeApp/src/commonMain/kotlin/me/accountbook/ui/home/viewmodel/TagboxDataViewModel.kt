package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxManager
import me.accountbook.data.manager.sync.SyncStateManager
import me.accountbook.data.manager.sync.TagboxSyncManager
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDataViewModel : ViewModel(), KoinComponent {
    private val dbManager: TagboxManager by inject()
    private val syncManager: TagboxSyncManager by inject()
    var syncState by mutableStateOf(SyncStateManager())
    var tagboxList by mutableStateOf<List<Tagbox>>(emptyList())
        private set
    var text by mutableStateOf("")

    fun moveTagbox(from: Int, to: Int) {
        viewModelScope.launch {
            tagboxList = tagboxList.toMutableList().apply {
                add(to, removeAt(from))
            }
        }
    }

    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            syncState.startLoading()
            syncManager.sync().also {
                syncState.isSynced(dbManager.isSynced())
            }
            initData()
            syncState.endLoading()
        }
    }

    suspend fun initData() {
        tagboxList = dbManager.queryUnDeleted() ?: run {
            LoggingUtil.logInfo("tagbox为空")
            return
        }
        tagboxList = tagboxList.sortedBy { it.position }
        dbManager.refreshTimestamp()
        syncState.isSynced(dbManager.isSynced())

    }

    fun updatePosition() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList.forEachIndexed { index, it ->
                dbManager.updatePosition(index, it.uuid)
            }
            syncState.isSynced(dbManager.isSynced())
        }
    }

    fun updateName(name: String, uuid: String) {
        dbManager.updateName(name, uuid)
        syncState.isSynced(dbManager.isSynced())
    }

    fun updateColor(color: Color, uuid: String) {
        dbManager.updateColor(color, uuid)
        syncState.isSynced(dbManager.isSynced())
    }

    fun insert(name: String, color: Color) {
        dbManager.insert(name, color)
        syncState.isSynced(dbManager.isSynced())
    }

    fun softDelete(uuid: String) {
        dbManager.softDelete(uuid)
        syncState.isSynced(dbManager.isSynced())
    }

}
