package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import me.accountbook.database.Tagbox
import me.accountbook.ui.viewmodel.TagboxViewModel

class TagboxEditViewModel : TagboxViewModel() {
    var isPopupVisible by mutableStateOf(false)
    var uuid by mutableStateOf("")
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)
    fun togglePopupVisible() {
        isPopupVisible = !isPopupVisible
    }

    fun initByTagbox(tagbox: Tagbox) {
        uuid = tagbox.uuid
        name = tagbox.name
        color = Color(tagbox.color)
    }

}