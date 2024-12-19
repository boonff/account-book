package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.data.manager.domain.TagboxDataManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxEditViewModel : ViewModel(), KoinComponent {
    protected val tagboxDataManager: TagboxDataManager by inject()

    var isPopupVisible by mutableStateOf(false)
    var uuid by mutableStateOf("")
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)

}