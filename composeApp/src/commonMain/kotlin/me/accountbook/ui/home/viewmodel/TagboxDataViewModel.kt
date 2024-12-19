package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxDataManager
import me.accountbook.data.manager.sync.SyncStateManager
import me.accountbook.data.manager.sync.TagboxSyncManager
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDataViewModel : ViewModel(), KoinComponent {
    private val dbManager: TagboxDataManager by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    var syncState by mutableStateOf(SyncStateManager())
    var tagboxList by mutableStateOf<List<Tagbox>>(emptyList())
        private set
    var text by mutableStateOf("")
    var isPopupVisible by mutableStateOf(false)
    var uuid by mutableStateOf("")
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)

    fun togglePopupVisible() {
        isPopupVisible = !isPopupVisible
    }

    fun initByTagbox(tagbox: Tagbox) {
        uuid = tagbox.uuid
        name = tagbox.name
        color = Color(tagbox.color)
    }
    fun moveTagbox(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList = tagboxList.toMutableList().apply {
                add(to, removeAt(from))
            }
        }
    }

    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            syncState.startLoading()
            tagboxSyncManager.sync().also {
                if (it)
                    syncState.isSynced(dbManager.isSynced())
                else
                    LoggingUtil.logError("同步失败")
            }
            initData()
            syncState.endLoading()
        }
    }

    private suspend fun loadTagboxList() {
        tagboxList = dbManager.fetchUnDeleted()?.sortedBy { it.position } ?: run {
            LoggingUtil.logInfo("tagbox为空")
            emptyList()
        }
    }

    suspend fun initData() {
        loadTagboxList()
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
        viewModelScope.launch(Dispatchers.IO) {
            dbManager.updateName(name, uuid)
            syncState.isSynced(dbManager.isSynced())
            loadTagboxList()
        }
    }

    fun updateColor(color: Color, uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbManager.updateColor(color, uuid)
            syncState.isSynced(dbManager.isSynced())
            loadTagboxList()
        }

    }

    fun insert(name: String, color: Color) {
        viewModelScope.launch(Dispatchers.IO) {
            dbManager.insert(name, color)
            syncState.isSynced(dbManager.isSynced())
            loadTagboxList()
        }

    }

    fun softDelete(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbManager.softDelete(uuid)
            syncState.isSynced(dbManager.isSynced())
            loadTagboxList()
        }

    }

}
