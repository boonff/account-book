package me.accountbook.ui.home.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AndroidTagboxDetailsViewModel : TagboxDetailsViewModel() {
    init {
        viewModelScope.launch {
            tagboxDataManager.tagboxList.collect {
                tagboxList = it
            }
        }
    }
}