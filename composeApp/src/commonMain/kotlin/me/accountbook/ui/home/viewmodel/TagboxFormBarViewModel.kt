package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.accountbook.data.manager.domain.TagboxManagerImpl
import me.accountbook.ui.home.compoents.ColorPalette
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class TagboxFormBarViewModel : ViewModel(), KoinComponent {
    private val tagboxManager: TagboxManagerImpl by inject()
    var text by mutableStateOf("")
        internal set

    fun insert() {
        viewModelScope.launch {
            val random = Random.nextInt(0, ColorPalette.colors.size)
            tagboxManager.insert(text, ColorPalette.colors[random])
            text = ""
        }
    }
}