package me.accountbook.network.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.koin.OAuthConfig
import me.accountbook.koin.getRedirectUri
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.FileUtil
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

abstract class LoginManagerImpl : LoginManager, KoinComponent {
    override val oauthConfig: OAuthConfig by inject()
    protected val fileStore: FileUtil by inject()
    private val httpClient = OkHttpClient()
    protected val serverUtil = ServerUtil()

    override fun isLoggedIn(): Boolean {
        return fileStore.isFileExist("token")//此文件名需要解耦。
    }

    protected fun getAuthorizationUrl(): String {
        val baseUrl = oauthConfig.oauthUrl
        val scope = "repo"
        return "$baseUrl?client_id=${oauthConfig.clientId}&response_type=code&redirect_uri=${oauthConfig.getRedirectUri()}&scope=$scope"//scope需要填写
    }

    protected suspend fun getAccessToken(): String {

        val authorizationCode = serverUtil.getAuthorizationCode()
        serverUtil.stopServer()

        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", authorizationCode) // 获取授权码
            .add("redirect_uri", oauthConfig.getRedirectUri())
            .add("client_id", oauthConfig.clientId)
            .add("client_secret", oauthConfig.clientSecret)
            .build()

        val request = Request.Builder()
            .url(oauthConfig.oauthTokenUrl)
            .post(requestBody)
            .build()
        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("response is not Successful")
        val responseBody = response.body?.string()
            ?:
                throw Exception("\"Error requesting access token: ${response.message}\"")
        val decodedResponse =
            withContext(Dispatchers.IO) {
                URLDecoder.decode(responseBody, StandardCharsets.UTF_8.name())
            }
        val tokenMap = decodedResponse.split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
        return tokenMap["access_token"] ?: throw Exception("tokenMap[\"access_token\"] is null")
    }

    // 撤销 Access Token
    protected suspend fun revokeAccessToken() {
        // 假设撤销 token 的 API 是 POST 请求，并且提供了一个 endpoint 来撤销 token
        val revokeUrl = "https://oauth-provider.com/revoke" // 替换为实际的撤销 URL
        val accessToken = getAccessToken() // 获取当前的 access token

        val requestBody = FormBody.Builder()
            .add("token", accessToken) // 把当前的 token 发送到撤销接口
            .build()

        val request = Request.Builder()
            .url(revokeUrl)
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Failed to revoke access token: ${response.message}")
        }

        // 成功撤销 token 后，清除本地存储的 token 文件
        fileStore.deleteFile("token") // 删除 token 文件或执行其他本地清理操作
    }


}