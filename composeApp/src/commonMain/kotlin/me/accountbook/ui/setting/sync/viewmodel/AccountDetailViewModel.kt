package me.accountbook.ui.setting.sync.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.network.GitHubApiService
import me.accountbook.network.LoginService
import me.accountbook.network.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AccountDetailViewModel : ViewModel(), KoinComponent {
    private val loginService: LoginService by inject()
    private val githubApi = GitHubApiService
    private val userService: UserService by inject()

    var isLoading by mutableStateOf(false)
    var userInfo by mutableStateOf("")
        private set

    fun isLogin(): Boolean {
        return userService.isLogin
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userService.logout()
        }

    }

    //获取 token 保存
    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            userService.login()
        }
    }
}