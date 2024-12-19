package me.accountbook.ui.setting.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel

class JvmLoginViewModel : LoginViewModel() {
    init {
        viewModelScope.launch(Dispatchers.Unconfined) {
            userManager.isLogin.collect {
                isLogin = it

            }
        }
    }
}