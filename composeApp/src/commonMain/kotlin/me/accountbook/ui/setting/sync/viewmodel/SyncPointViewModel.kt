package me.accountbook.ui.setting.sync.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.accountbook.utils.SyncUtil

class SyncPointViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var syncSuccess by mutableStateOf(true)
        private set

    fun sync(
        reLoadData: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            syncSuccess = SyncUtil.sync()
            if (syncSuccess) {
                reLoadData()
            }
            isLoading = false
        }
    }
}