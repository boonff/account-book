package me.accountbook.ui.setting.sync.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.accountbook.network.manager.UserManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class LoginViewModel : ViewModel(), KoinComponent {
    protected val userManager: UserManager by inject()

    var isLogin by mutableStateOf(false)
        protected set

    var isLoading by mutableStateOf(false)
    var userInfo by mutableStateOf("")

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
