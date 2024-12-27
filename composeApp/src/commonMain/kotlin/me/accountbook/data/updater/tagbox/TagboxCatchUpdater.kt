// me.accountbook.data.manager.domain/TagboxListUpdater.kt
package me.accountbook.data.updater.tagbox

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.accountbook.database.Tagbox


/*
Todo: 更名为 tagboxCatchUpdaterManager，并移动到data.local.manager.tagbox包
 */
class TagboxCatchUpdater {
    private val _catchList = MutableStateFlow<List<Tagbox>>(emptyList())
    val catchList: StateFlow<List<Tagbox>> get() = _catchList

    fun updateList(newList: List<Tagbox>) {
        _catchList.value = newList
    }

    fun sortedByPosition() {
        _catchList.value = _catchList.value.sortedBy { it.position }
    }

    fun sortedByTimestamp() {
        _catchList.value = _catchList.value.sortedBy { it.timestamp }
    }

    fun addItem(data: Tagbox) {
        _catchList.value += data
    }

    fun updateColor(uuid: String, color: Color) {
        _catchList.value = _catchList.value.map {
            if (it.uuid == uuid) {
                it.copy(color = color.toArgb().toUInt().toLong())
            } else {
                it
            }
        }
    }

    fun updateName(uuid: String, name: String) {
        _catchList.value = _catchList.value.map {
            if (it.uuid == uuid) {
                it.copy(name = name)
            } else {
                it
            }
        }
    }

    fun updatePosition(uuid: String, position: Int) {
        _catchList.value = _catchList.value.map {
            if (it.uuid == uuid) {
                it.copy(position = position.toLong())
            } else {
                it
            }
        }
    }

    fun removeItem(uuid: String) {
        _catchList.value = _catchList.value.filterNot { it.uuid == uuid }
    }

}