package me.accountbook.network.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.network.service.GitHubApiService
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val repoName: String = "test"
private const val rootName: String = "accountbook/"

class RepositoryManager : KoinComponent {
    private var username: String? = null
    private val githubApi = GitHubApiService
    private val userManager: UserManager by inject()

    // fetchUsername 方法也需要执行网络请求，因此也放入 withContext(Dispatchers.IO)
    private suspend fun fetchUsername(): String? {
        if (username != null) return username
        return withContext(Dispatchers.IO) {
            userManager.authorize { token ->
                githubApi.fetchUserName(token).also { username = it }
            } as? String?
        }
    }

    suspend fun upload(pathName: String, protoBufBytes: ByteArray): Boolean {
        val username = fetchUsername() ?: run {
            LoggingUtil.logDebug("username获取失败")
            return false
        }
        return withContext(Dispatchers.IO) {
            userManager.authorize { token ->
                githubApi.uploadProtoBufToRepo(
                    token,
                    username,
                    repoName,
                    rootName + pathName,
                    protoBufBytes
                )
            } as? Boolean ?: false
        }
    }

    suspend fun fetch(pathName: String): ByteArray? {
        val username = fetchUsername() ?: run {
            LoggingUtil.logDebug("username获取失败")
            return null
        }
        return withContext(Dispatchers.IO) {
            userManager.authorize { token ->
                githubApi.fetchFileContentAsByteArray(
                    token,
                    username,
                    repoName,
                    rootName + pathName
                )
            } as? ByteArray?
        }
    }

    suspend fun create(): Boolean {
        val username = fetchUsername() ?: run {
            LoggingUtil.logDebug("username获取失败")
            return false
        }
        return withContext(Dispatchers.IO) {
            userManager.authorize { token ->
                githubApi.createPrivateRepo(
                    token,
                    username,
                    repoName
                )
            } as? Boolean ?: false
        }
    }
}
