package me.accountbook.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxListUpdater
import me.accountbook.data.manager.domain.TagboxManagerImpl
import me.accountbook.data.manager.sync.TagboxSyncManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDetailViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxManagerImpl by inject()
    private val tagboxListUpdater: TagboxListUpdater by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    val syncStateManager get() = tagboxManager.syncStateManager
    var tagboxList = tagboxListUpdater.tagboxList


    fun moveTagbox(from: Int, to: Int) {
        val updatedList = tagboxList.value.toMutableList().apply {
            add(to, removeAt(from))
        }
        tagboxListUpdater.updateList(updatedList)
    }

    fun updatePosition() {
        viewModelScope.launch(Dispatchers.Default) {
            val updatedPositions = tagboxList.value.mapIndexed { index, tagbox ->
                tagbox.uuid to index
            }
            tagboxManager.updatePositions(updatedPositions)
        }
    }


    fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxSyncManager.sync()
            tagboxManager.fetchUnDeleted()
            tagboxManager.sortedTagboxByPosition()
        }
    }

}
