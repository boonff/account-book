package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxManagerImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxEditViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxManagerImpl by inject()
    var uuid by mutableStateOf("")
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)

    fun updateName(name: String, uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxManager.updateName(name, uuid)

        }
    }

    fun updateColor(color: Color, uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxManager.updateColor(color, uuid)
        }
    }

    fun softDelete(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxManager.softDelete(uuid)
        }

    }
}