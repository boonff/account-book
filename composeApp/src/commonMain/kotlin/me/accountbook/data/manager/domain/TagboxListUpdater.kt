package me.accountbook.data.manager.domain

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.accountbook.database.Tagbox

class TagboxListUpdater {
    private val _tagboxList = MutableStateFlow<List<Tagbox>>(emptyList())
    val tagboxList: StateFlow<List<Tagbox>> get() = _tagboxList

    // 更新标签列表中的指定标签项
    fun updateItem(uuid: String, newTagbox: Tagbox) {
        _tagboxList.value = _tagboxList.value.map {
            if (it.uuid == uuid) newTagbox else it
        }
    }

    // 向标签列表中添加新标签
    fun addItem(name: String, color: Color) {
        _tagboxList.value = _tagboxList.value + Tagbox(
            uuid = "",

        )
    }

    // 从标签列表中移除指定标签
    fun removeItem(uuid: String) {
        _tagboxList.value = _tagboxList.value.filterNot { it.uuid == uuid }
    }

    // 更新标签列表
    fun updateList(newList: List<Tagbox>) {
        _tagboxList.value = newList
    }
}
