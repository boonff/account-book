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

open class HomeScreenViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxManagerImpl by inject()
    private val tagboxListUpdater: TagboxListUpdater by inject()
    private val tagboxSyncManager: TagboxSyncManager by inject()
    var tagboxList = tagboxListUpdater.tagboxList
    val tagboxStateManager = tagboxManager.syncStateManager

    fun syncTagbox() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxSyncManager.sync()
            tagboxManager.fetchUnDeleted()
            tagboxManager.sortedTagboxByPosition()
        }

    }


}