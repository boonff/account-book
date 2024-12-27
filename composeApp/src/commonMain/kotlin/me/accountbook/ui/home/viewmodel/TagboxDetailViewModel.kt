package me.accountbook.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.accountbook.data.local.model.TableKey
import me.accountbook.data.sync.domain.SyncStateManagers
import me.accountbook.data.updater.tagbox.TagboxCatchUpdater
import me.accountbook.data.updater.tagbox.TagboxUpdateManager
import me.accountbook.data.sync.tagbox.TagboxSyncManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDetailViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxUpdateManager by inject()
    private val tagboxCatchUpdater: TagboxCatchUpdater by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    val syncStateManager get() = SyncStateManagers.getSyncStateManager(TableKey.Tagbox)
    var tagboxList  = tagboxCatchUpdater.catchList


    fun moveTagbox(from: Int, to: Int) {
        val updatedList = tagboxList.value.toMutableList().apply {
            add(to, removeAt(from))
        }
        tagboxCatchUpdater.updateList(updatedList)
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
            withContext(NonCancellable) {
                tagboxSyncManager.sync()
                tagboxManager.fetchUnDeleted()
                tagboxManager.sortedListByPosition()
            }
        }
    }

}
