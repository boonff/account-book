package me.accountbook.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagDetailsViewModel : ViewModel(), KoinComponent {
    private val dbHelper: DatabaseHelper by inject()
    var tagbox by mutableStateOf<List<Tagbox>>(emptyList())
        private set
    var isShow = mutableStateOf(false)
        private set

    suspend fun loadTags() {
        tagbox = dbHelper.queryAllTagBox()
    }

    fun toggleShow() {
        isShow.value = !isShow.value
    }
}
