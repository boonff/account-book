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

open class HomeScreenViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxUpdateManager by inject()
    private val tagboxCatchUpdater: TagboxCatchUpdater by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    var tagboxList = tagboxCatchUpdater.catchList
    val tagboxStateManager get() = SyncStateManagers.getSyncStateManager(TableKey.Tagbox)

    fun syncTagbox() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(NonCancellable) {
                tagboxSyncManager.sync()
                tagboxManager.fetchUnDeleted()
                tagboxManager.sortedListByPosition()

            }
        }

    }


}