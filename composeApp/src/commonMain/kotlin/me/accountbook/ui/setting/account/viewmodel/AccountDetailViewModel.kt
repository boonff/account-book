package me.accountbook.ui.setting.account.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.login.LoginManagerImpl
import me.accountbook.network.GitHubApiService
import me.accountbook.network.GitHubUser
import me.accountbook.network.login.LoginManager
import me.accountbook.utils.serialization.CodecUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AccountDetailViewModel(
    private val dbHelper: DatabaseHelper
) : ViewModel(), KoinComponent {
    private val loginManager: LoginManager by inject()
    private val githubApi = GitHubApiService


    var isLogin by mutableStateOf(loginManager.isLoggedIn())
        private set

    var isLoading by mutableStateOf(false)
    var userInfo by mutableStateOf<GitHubUser?>(null)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    suspend fun fetchFile(){

    }

    suspend fun uploadProtoBufToRepo() {

        githubApi.uploadProtoBufToRepo(
            CodecUtil.serializationDatabase()
        )
    }

    suspend fun fetchGitHubUserInfo() {
        isLoading = true
        try {
            val user = githubApi.fetchUserInfo()
            userInfo = user
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    suspend fun createPrivateRepo() {
        githubApi.createPrivateRepo()
    }

    //获取 token 保存
    suspend fun initToken() {
        if (loginManager.isLoggedIn()) return //token文件已存在，跳过获取token的过程
        loginManager.openLoginPage()
        try {
            loginManager.saveAccessToken()
            loginManager.readAccessToken()?.let { githubApi.setToken(it) }
                ?: throw Exception("文件中读取到的token为空")
            isLogin = true
        } catch (e: Exception) {
            println("login Error :${e.message}")
        }
    }
}