package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.ui.viewmodel.TagboxViewModel

class TagboxDetailsViewModel : TagboxViewModel() {
    var text by mutableStateOf("")
    fun moveTagbox(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList = tagboxList.toMutableList().apply {
                add(to, removeAt(from))
            }
        }

    }

    fun loadSortedTagbox() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList = dbHelper.queryUndeletedTagBox()
            tagboxList = tagboxList.toMutableList().apply {
                sortBy {
                    it.position
                }

            }
        }
    }

    fun updatePosition() {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList.forEachIndexed { index, it ->
                dbHelper.updateTagboxPosition(index, it.uuid)
            }

        }
    }
}
