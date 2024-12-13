package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.data.model.SerTagbox

class TagboxEditViewModel : TagboxDataViewModel() {
    var isPopupVisible by mutableStateOf(false)
    var uuid by mutableStateOf("")
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)
    fun togglePopupVisible() {
        isPopupVisible = !isPopupVisible
    }

    fun initByTagbox(tagbox: SerTagbox) {
        uuid = tagbox.uuid
        name = tagbox.name
        color = Color(tagbox.color)
    }

}