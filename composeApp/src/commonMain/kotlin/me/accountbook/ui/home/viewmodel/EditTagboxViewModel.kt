package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.common.viewmodel.BaseTagboxVIewModel

class EditTagboxViewModel(dbHelper: DatabaseHelper) : BaseTagboxVIewModel(dbHelper) {
    var isPopupVisible by mutableStateOf(false)
    var tagboxId by mutableStateOf(0)
    var name by mutableStateOf("")
    var color by mutableStateOf(Color.Transparent)
    fun togglePopupVisible() {
        isPopupVisible = !isPopupVisible
    }

    fun initByTagbox(tagbox: Tagbox) {
        tagboxId = tagbox.tagbox_id.toInt()
        name = tagbox.name
        color = Color(tagbox.color.toULong())
    }

}