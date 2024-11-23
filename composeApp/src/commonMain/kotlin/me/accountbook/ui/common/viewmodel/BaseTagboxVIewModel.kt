package me.accountbook.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper

abstract class BaseTagboxVIewModel(
    protected val dbHelper: DatabaseHelper
) : ViewModel() {
    var errorMessage by mutableStateOf("")
    protected set
    var tagboxs by mutableStateOf<List<Tagbox>>(emptyList())
    protected set

    suspend fun loadTagbox() {
        tagboxs = dbHelper.queryAllTagBox()

    }

    suspend fun loadSortedTagbox() {
        tagboxs = dbHelper.queryAllTagBox()
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

    suspend fun updateTagboxName(name: String, tagboxId: Int) {
        dbHelper.updateTagboxName(name, tagboxId)
    }

    suspend fun updateTagboxColor(color: Color, tagboxId: Int) {
        dbHelper.updateTagboxColor(color, tagboxId)
    }

    suspend fun deleteTagboxById(tagboxId: Int) {
        dbHelper.deleteTagBoxById(tagboxId)
    }
}
