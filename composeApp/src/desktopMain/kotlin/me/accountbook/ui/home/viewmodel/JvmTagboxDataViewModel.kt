package me.accountbook.ui.home.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JvmTagboxDataViewModel:TagboxDataViewModel() {
    init {
        viewModelScope.launch(Dispatchers.Unconfined){
            initData()
        }
    }
}