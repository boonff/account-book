package me.accountbook.ui.home.viewmodel

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.common.viewmodel.BaseTagboxVIewModel

class ReorderTagboxViewModel(dbHelper: DatabaseHelper) : BaseTagboxVIewModel(dbHelper) {
    var text by mutableStateOf("")
    fun moveTagbox(from: Int, to: Int) {
        tagboxs = tagboxs.toMutableList().apply {
            add(to, removeAt(from))
        }
    }

    suspend fun updatePosition() {
        tagboxs.forEachIndexed { index, it ->
            dbHelper.updateTagboxPosition(index, it.tagbox_id.toInt())
        }

    }
}
