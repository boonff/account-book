package me.accountbook.network.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
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
    private val repositoryManager: RepositoryManager by inject()

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
        return withContext(Dispatchers.IO) {  // 使用 I/O 调度器处理
            this@UserManager.token = token
            saveTokenFile(token).also {
                if (!it) LoggingUtil.logDebug("token文件保存失败")
            }
            _isLogin.value = true
        }
    }

    private suspend fun logoutSuccess() {
        return withContext(Dispatchers.IO) {  // 使用 I/O 调度器处理
            this@UserManager.token = null
            deleteTokenFile().also {
                if (!it) LoggingUtil.logDebug("token文件删除失败")
            }
            _isLogin.value = false
        }
    }

    suspend fun initUser(initOther: suspend () -> Unit): Boolean {
        return withContext(Dispatchers.IO) {  // 使用 I/O 调度器处理
            LoggingUtil.logInfo("run initUser")
            val token = readTokenFile() ?: run {
                LoggingUtil.logInfo("token 文件读取失败")
                return@withContext false
            }
            return@withContext if (loginService.checkToken(token)) {
                loginSuccess(token)
                repositoryManager.create()
                initOther()
                true
            } else {
                LoggingUtil.logDebug("token 检查未通过")
                false
            }
        }
    }

    suspend fun login(): Boolean {
        return withContext(Dispatchers.IO) {  // 使用 I/O 调度器处理
            token?.let {
                if (loginService.checkToken(it))
                    return@withContext false
            }
            deleteTokenFile()
            ServerUtil.startServer()
            loginService.openLoginPage()

            val token = loginService.getToken() ?: run {
                LoggingUtil.logDebug("token获取失败")
                return@withContext false
            }
            return@withContext if (loginService.checkToken(token)) {
                loginSuccess(token)
                repositoryManager.create()
                true
            } else {
                LoggingUtil.logDebug("获取到的token无效")
                false
            }
        }
    }

    suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {  // 使用 I/O 调度器处理
            if (!isLogin.value) {
                LoggingUtil.logDebug("用户未登录")
                return@withContext true
            }

            val token = token ?: run {
                readTokenFile() ?: run {
                    LoggingUtil.logDebug("token文件读取失败")
                    return@withContext false
                }
            }

            return@withContext if (loginService.revokeToken(token)) {
                LoggingUtil.logInfo("token成功销毁")
                logoutSuccess()
                true
            } else {
                LoggingUtil.logInfo("token销毁失败")
                false
            }
        }
    }

    abstract suspend fun saveTokenFile(token: String): Boolean

    abstract suspend fun deleteTokenFile(): Boolean

    abstract fun readTokenFile(): String?
}


