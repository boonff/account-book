package me.accountbook.ui.setting.sync.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncPointViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var syncSuccess by mutableStateOf(true)
        private set
}