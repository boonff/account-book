package me.accountbook.ui.setting.sync.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.accountbook.network.manager.UserManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val userManager: UserManager by inject()
    var isLogin by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
    var userInfo by mutableStateOf("")
    suspend fun initLogin() {
        userManager.isLogin.collect { loggedIn ->
            isLogin = loggedIn
        }

    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userManager.logout()
        }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            userManager.login()
        }
    }
}
