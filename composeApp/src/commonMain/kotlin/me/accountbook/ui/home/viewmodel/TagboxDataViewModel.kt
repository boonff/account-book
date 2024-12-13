package me.accountbook.ui.home.viewmodel

import androidx.annotation.ColorRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.TagboxSyncManager
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.repository.TagboxRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDataViewModel : ViewModel(), KoinComponent {
    private val tagboxRepository: TagboxRepository by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    var isSynced by mutableStateOf(false)
        private set
    var tagboxList by mutableStateOf<List<SerTagbox>>(emptyList())
        private set
    var text by mutableStateOf("")
    fun moveTagbox(from: Int, to: Int) {
        viewModelScope.launch {
            tagboxList = tagboxList.toMutableList().apply {
                add(to, removeAt(from))
            }
            isSynced = false
        }
    }

    fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {
            isSynced = (tagboxSyncManager.sync())
        }
    }

    fun loadSortedTagbox() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList = tagboxRepository.query() ?: emptyList()
            tagboxList = tagboxList.sortedBy { it.position }
            isSynced = false
        }
    }

    fun updatePosition() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList.forEachIndexed { index, it ->
                tagboxRepository.updatePosition(index, it.uuid)
            }
            isSynced = false
        }
    }

    fun updateName(name: String, uuid: String) {
        tagboxRepository.updateName(name, uuid)
        isSynced = false
    }

    fun updateColor(color: Color, uuid: String) {
        tagboxRepository.updateColor(color, uuid)
        isSynced = false
    }

    fun insert(name: String, color: Color) {
        tagboxRepository.insert(name, color)
        isSynced = false
    }

    fun softDelete(uuid: String) {
        tagboxRepository.softDelete(uuid)
        isSynced = false
    }

}
