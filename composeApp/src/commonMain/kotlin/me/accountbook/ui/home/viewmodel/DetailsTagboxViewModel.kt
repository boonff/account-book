package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.accountbook.ui.viewmodel.TagboxViewModel

class DetailsTagboxViewModel : TagboxViewModel() {
    var text by mutableStateOf("")
    fun moveTagbox(from: Int, to: Int) {
        tagboxs = tagboxs.toMutableList().apply {
            add(to, removeAt(from))
        }
    }

    fun loadSortedTagbox() {
        tagboxs = dbHelper.queryUndeletedTagBox()
        tagboxs = tagboxs.toMutableList().apply {
            sortBy {
                it.position
            }

        }
    }

    fun updatePosition() {
        tagboxs.forEachIndexed { index, it ->
            dbHelper.updateTagboxPosition(index, it.uuid)
        }

    }
}
