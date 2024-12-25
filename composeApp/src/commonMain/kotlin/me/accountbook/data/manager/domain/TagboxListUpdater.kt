// me.accountbook.data.manager.domain/TagboxListUpdater.kt
package me.accountbook.data.manager.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.accountbook.database.Tagbox

class TagboxListUpdater {
    private val _tagboxList = MutableStateFlow<List<Tagbox>>(emptyList())
    val tagboxList: StateFlow<List<Tagbox>> get() = _tagboxList

    fun updateList(newList: List<Tagbox>) {
        _tagboxList.value = newList
    }

    fun sortedByPosition() {
        _tagboxList.value = _tagboxList.value.sortedBy { it.position }
    }

    fun sortedByTimestamp() {
        _tagboxList.value = _tagboxList.value.sortedBy { it.timestamp }
    }

    fun addItem(data: Tagbox) {
        _tagboxList.value += data
    }

    fun updateColor(uuid: String, color: Color) {
        _tagboxList.value = _tagboxList.value.map {
            if (it.uuid == uuid) {
                it.copy(color = color.toArgb().toUInt().toLong())
            } else {
                it
            }
        }
    }

    fun updateName(uuid: String, name: String) {
        _tagboxList.value = _tagboxList.value.map {
            if (it.uuid == uuid) {
                it.copy(name = name)
            } else {
                it
            }
        }
    }

    fun updatePosition(uuid: String, position: Int) {
        _tagboxList.value = _tagboxList.value.map {
            if (it.uuid == uuid) {
                it.copy(position = position.toLong())
            } else {
                it
            }
        }
    }

    fun removeItem(uuid: String) {
        _tagboxList.value = _tagboxList.value.filterNot { it.uuid == uuid }
    }

}