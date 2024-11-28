package me.accountbook.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.database.Tagbox
import me.accountbook.database.DatabaseHelper

abstract class BaseTagboxVIewModel(
    protected val dbHelper: DatabaseHelper
) : ViewModel() {
    var errorMessage by mutableStateOf("")
        protected set
    var tagboxs by mutableStateOf<List<Tagbox>>(emptyList())
        protected set

    suspend fun loadTagbox() {
        tagboxs = dbHelper.queryUndeletedTagBox()

    }

    suspend fun loadSortedTagbox() {
        tagboxs = dbHelper.queryUndeletedTagBox()
        tagboxs = tagboxs.toMutableList().apply {
            sortBy {
                it.position
            }

        }
    }

    suspend fun insertTagbox(name: String, color: Color) {
        if (name.length in 1..100)
            try {
                dbHelper.insertTagBox(name, color)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }


    }

    suspend fun updateTagboxName(name: String, uuid: String) {
        dbHelper.updateTagboxName(name, uuid)
    }

    suspend fun updateTagboxColor(color: Color, uuid: String) {
        dbHelper.updateTagboxColor(color, uuid)
    }

    suspend fun softDeleteTagbox(uuid: String) {
        dbHelper.softDeleteTagbox(uuid)
    }
}
