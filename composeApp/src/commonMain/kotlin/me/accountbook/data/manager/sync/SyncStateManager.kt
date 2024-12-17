package me.accountbook.data.manager.sync


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.accountbook.network.manager.UserManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SyncStateManager : KoinComponent {
    var isLoading by mutableStateOf(false)
        private set
    private var syncState by mutableStateOf(SyncState.UNKNOWN)

    fun isSynced(state: Boolean) {
        syncState = if (state)
            SyncState.SYNCED
        else
            SyncState.UNSYNCED
    }


    fun startLoading() {
        isLoading = true
    }

    fun endLoading() {
        isLoading = false
    }

    fun getColorForState(): Color {
        return when (syncState) {
            SyncState.SYNCED -> Color.Green
            SyncState.UNSYNCED -> Color.Red
            SyncState.UNKNOWN -> Color.Yellow
        }
    }
}

enum class SyncState { SYNCED, UNKNOWN, UNSYNCED }