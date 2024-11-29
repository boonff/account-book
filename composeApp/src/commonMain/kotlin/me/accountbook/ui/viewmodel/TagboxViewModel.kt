package me.accountbook.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import me.accountbook.database.Tagbox
import me.accountbook.database.DatabaseHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class TagboxViewModel : ViewModel(), KoinComponent {
    protected val dbHelper: DatabaseHelper by inject()
     var tagboxs by mutableStateOf<List<Tagbox>>(emptyList())
        protected set

    fun loadTagbox() {
        tagboxs = dbHelper.queryUndeletedTagBox()

    }
    fun insertTagbox(name: String, color: Color) {
        if (name.length in 1..100)
            dbHelper.insertTagBox(name, color)
    }

    fun updateTagboxName(name: String, uuid: String) {
        dbHelper.updateTagboxName(name, uuid)
    }

    fun updateTagboxColor(color: Color, uuid: String) {
        dbHelper.updateTagboxColor(color, uuid)
    }

    fun softDeleteTagbox(uuid: String) {
        dbHelper.softDeleteTagbox(uuid)
    }
}
