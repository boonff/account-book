package me.accountbook.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.database.Database
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseTagboxVIewModel(
    protected val dbHelper: DatabaseHelper
) : ViewModel() {
    var tagbox by mutableStateOf<List<Tagbox>>(emptyList())
        protected set

    suspend fun loadTagbox() {
        tagbox = dbHelper.queryAllTagBox()

    }
    suspend fun loadSortedTagbox() {
        tagbox = dbHelper.queryAllTagBox()
        tagbox = tagbox.toMutableList().apply {
            sortBy {
                it.position
            }

        }

    }

    suspend fun insertTagbox(name: String, color: Color) {
        if (name.length in 1..9)
            dbHelper.insertTagBox(name, color.value.toLong())

    }
}