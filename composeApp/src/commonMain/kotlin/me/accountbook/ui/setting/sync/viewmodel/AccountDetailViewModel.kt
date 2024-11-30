package me.accountbook.ui.setting.sync.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.network.GitHubApiService
import me.accountbook.network.login.LoginManager
import me.accountbook.utils.serialization.CodecUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AccountDetailViewModel : ViewModel(), KoinComponent {
    private val loginManager: LoginManager by inject()
    private val githubApi = GitHubApiService


    var isLogin by mutableStateOf(loginManager.isLoggedIn())
        private set

    var isLoading by mutableStateOf(false)
    var userInfo by mutableStateOf("")
        private set
    var error by mutableStateOf<String?>(null)
        private set

    suspend fun revokeAccessToken() {
        if (loginManager.checkAccessToken())
            loginManager.revokeAccessToken()
        loginManager.deleteAccessToken()

    }

    suspend fun checkToken() {
        loginManager.checkAccessToken()
    }

    suspend fun uploadProtoBufToRepo() {

        githubApi.uploadProtoBufToRepo(
            CodecUtil.serializationDatabase()
        )
    }

    suspend fun fetchGitHubUserInfo() {
        isLoading = true
        try {
            val user = githubApi.fetchUserName() ?: "error"
            userInfo = user
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }


    //获取 token 保存
    fun initToken() {
        viewModelScope.launch(Dispatchers.IO) {
            if (loginManager.isLoggedIn()) return@launch //token文件已存在，跳过获取token的过程
            loginManager.openLoginPage()
            try {
                loginManager.saveAccessToken()
                loginManager.readAccessToken()?.let { githubApi.setToken(it) }
                    ?: throw Exception("文件中读取到的token为空")
                isLogin = true
                githubApi.createPrivateRepo()
            } catch (e: Exception) {
                println("login Error :${e.message}")
            }
        }

    }
}