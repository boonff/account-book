package me.accountbook.ui.setting.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel

class AndroidLoginViewModel : LoginViewModel() {
    init {
        viewModelScope.launch(Dispatchers.Main) {
            userManager.isLogin.collect {
                isLogin = it
            }
        }
    }
}