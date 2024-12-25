package me.accountbook.data.manager.sync

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class SyncStateManager : KoinComponent {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _syncState = MutableStateFlow(SyncState.UNKNOWN)
    val syncState: StateFlow<SyncState> = _syncState

    fun setSynced(state: Boolean) {
        _syncState.value = if (state)
            SyncState.SYNCED
        else
            SyncState.UNSYNCED
    }

    fun startLoading() {
        _isLoading.value = true
    }

    fun endLoading() {
        _isLoading.value = false
    }
}

enum class SyncState { SYNCED, UNKNOWN, UNSYNCED }