package me.accountbook.network

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.FileUtil
import me.accountbook.utils.serialization.SerializableDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class UserService : KoinComponent {
    var isLogin by mutableStateOf(false)
        private set
    protected val tokenPath: String = "token"
    protected var token: String? = null
    private var username: String? = null
    private val repoName: String = "test"
    private val repoPath: String = "database.bat"
    private val loginService: LoginService by inject()


    suspend fun initUser(): Boolean {
        token = readTokenFile()
        token?.let {
            isLogin = loginService.checkToken(it)//应用启动时检测登录状态
            if (isLogin)
                username = GitHubApiService.fetchUserName(it)
            return username != null
        }
        return false
    }

    suspend fun login(): Boolean {
        //如果已经登录，不能重复登录
        token?.let {
            if (loginService.checkToken(it))
                return false
        }
        deleteTokenFile()
        ServerUtil.startServer()
        loginService.openLoginPage()

        loginService.getToken()?.let { token ->
            isLogin = loginService.checkToken(token)
            if (isLogin) {
                saveTokenFile(token)
                this.token = token
                username = GitHubApiService.fetchUserName(token)
                username?.let { username ->
                    return GitHubApiService.createPrivateRepo(
                        token,
                        username,
                        repoName
                    )//创建仓库后登录成功，之后不必检查仓库存在性
                } ?: return false
            }
        }
        return false
    }

    suspend fun logout(): Boolean {
        if (!isLogin) return true

        token = readTokenFile()
        token?.let {
            loginService.revokeToken(it)
            token = null
            isLogin = !deleteTokenFile()
            return !isLogin
        } ?: run {
            isLogin = false
            return true
        }
    }

    suspend fun uploadToRepo(protoBufData: SerializableDatabase): Boolean {
        val token = token ?: return false
        val username = username ?: return false
        return GitHubApiService.uploadProtoBufToRepo(
            token,
            username,
            repoName,
            repoPath,
            protoBufData
        )
    }

    suspend fun fetchFile(): ByteArray? {
        val token = token ?: return null
        val username = username ?: return null
        return GitHubApiService.fetchFileContentAsByteArray(
            token,
            username,
            repoName,
            repoPath
        )
    }

    abstract suspend fun saveTokenFile(token: String): Boolean

    abstract suspend fun deleteTokenFile(): Boolean

    abstract fun readTokenFile(): String?

}