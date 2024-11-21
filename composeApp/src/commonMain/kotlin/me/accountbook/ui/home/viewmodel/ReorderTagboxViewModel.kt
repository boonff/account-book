package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.common.viewmodel.BaseTagboxVIewModel

class ReorderTagboxViewModel(dbHelper: DatabaseHelper) : BaseTagboxVIewModel(dbHelper) {
    var isFormBarVisible by mutableStateOf(false)
        private set
    var isPopupVisible by mutableStateOf(false)
        private set
    var popupPosition by mutableStateOf(Offset(0f, 0f))
    var text by mutableStateOf("")



    fun toggleFormBarVisible() {
        isFormBarVisible = !isFormBarVisible
    }

    fun togglePopupVisible() {
        isPopupVisible = !isPopupVisible
    }

    fun moveTagbox(from: Int, to: Int) {
        tagbox = tagbox.toMutableList().apply {
            add(to, removeAt(from))
        }
    }

    suspend fun updatePosition() {
        tagbox.forEachIndexed { index, it ->
            dbHelper.updateTagboxPosition(index, it.tagbox_id)
        }

    }
}
