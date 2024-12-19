package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxDataManager
import me.accountbook.database.Tagbox
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TagboxDetailsViewModel : ViewModel(), KoinComponent {
    protected val tagboxDataManager: TagboxDataManager by inject()
    var tagboxList by mutableStateOf<List<Tagbox>>(emptyList())
        protected set

    fun moveTagbox(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxList = tagboxList.toMutableList().apply {
                add(to, removeAt(from))
            }
        }
    }
    fun updatePosition(){

    }

    fun insertTagbox(name: String, color: Color) {
        viewModelScope.launch(Dispatchers.IO) {
            tagboxDataManager.insert(name, color)
        }
    }

}