package me.accountbook.network.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.accountbook.network.service.GitHubApiService
import me.accountbook.network.service.LoginService
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class UserManager : KoinComponent {
    @Volatile
    private var _isLogin = MutableStateFlow(false)  // 需要将它包装为 MutableStateFlow
    val isLogin: StateFlow<Boolean> get() = _isLogin  // 提供只读的 StateFlow
    protected val tokenPath: String = "token"
    private var token: String? = null
    private val loginService: LoginService by inject()

    suspend fun authorize(request: suspend (token: String) -> Any?): Any? {
        if (!_isLogin.value) {
            LoggingUtil.logDebug("用户未登录")
            return null
        }

        return token?.let {
            request(it)
        } ?: run {
            LoggingUtil.logDebug("token 为空")
            return null
        }
    }

    private suspend fun loginSuccess(token: String) {
        this.token = token
        saveTokenFile(token).also { if (!it) LoggingUtil.logDebug("token文件保存失败") }
        _isLogin.value = true
    }

    private suspend fun logoutSuccess() {
        token = null
        deleteTokenFile().also { if (!it) LoggingUtil.logDebug("token文件删除失败") }
        _isLogin.value = false
    }

    suspend fun initUser(initOther: suspend () -> Unit): Boolean {
        LoggingUtil.logInfo("run initUser")
        val token = readTokenFile() ?: run {
            LoggingUtil.logInfo("token 文件读取失败")
            return false
        }
        return if (loginService.checkToken(token)) {
            loginSuccess(token)
            initOther()
            true
        } else {
            LoggingUtil.logDebug("token 检查未通过")
            false
        }
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

        val token = loginService.getToken() ?: run {
            LoggingUtil.logDebug("token获取失败")
            return false
        }
        return if (loginService.checkToken(token)) {
            loginSuccess(token)
            true
        } else {
            LoggingUtil.logDebug("获取到的token无效")
            false
        }

    }

    suspend fun logout(): Boolean {
        if (!isLogin.value) {
            LoggingUtil.logDebug("用户未登录")
            return true
        }

        val token = token ?: run {
            readTokenFile() ?: run {
                LoggingUtil.logDebug("token文件读取失败")
                return false
            }
        }

        return if (loginService.revokeToken(token)) {
            LoggingUtil.logInfo("token成功销毁")
            logoutSuccess()
            true
        } else {
            LoggingUtil.logInfo("token销毁失败")
            false
        }
    }

    abstract suspend fun saveTokenFile(token: String): Boolean

    abstract suspend fun deleteTokenFile(): Boolean

    abstract fun readTokenFile(): String?

}


