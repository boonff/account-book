package me.accountbook.ui.common.components.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.data.sync.domain.SyncState
import me.accountbook.data.sync.domain.SyncStateManager

class SyncPointViewModel(private val syncStateManager: SyncStateManager) : ViewModel() {

    val isLoading get() = syncStateManager.isLoading
    val syncState get() = syncStateManager.syncState

    fun getColorForState(syncState: SyncState): Color {
        return when (syncState) {
            SyncState.SYNCED -> Color.Green
            SyncState.UNSYNCED -> Color.Red
            SyncState.UNKNOWN -> Color.Yellow
        }

    }

}