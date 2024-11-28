package me.accountbook.network.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.koin.OAuthConfig
import me.accountbook.koin.getRedirectUri
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.FileUtil
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.HttpRetryException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

abstract class LoginManagerImpl : LoginManager, KoinComponent {
    protected val tokenPath: String = "token"

    override val oauthConfig: OAuthConfig by inject()
    protected val fileStore: FileUtil by inject()
    private val loginManager: LoginManager by inject()
    private val httpClient = OkHttpClient()
    protected val serverUtil = ServerUtil

    override fun isLoggedIn(): Boolean {
        return fileStore.isFileExist(tokenPath)//此文件名需要解耦。
    }

    protected fun getAuthorizationUrl(): String {
        val baseUrl = oauthConfig.oauthUrl
        val scope = "repo"
        return "$baseUrl?client_id=${oauthConfig.clientId}&response_type=code&redirect_uri=${oauthConfig.getRedirectUri()}&scope=$scope"//scope需要填写
    }

    protected suspend fun getAccessToken(): String {
        val authorizationCode = serverUtil.getAuthorizationCode()
        serverUtil.stopServer()
        return withContext(Dispatchers.IO) {
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
                ?: throw Exception("\"Error requesting access token: ${response.message}\"")
            val decodedResponse = URLDecoder.decode(responseBody, StandardCharsets.UTF_8.name())
            val tokenMap = decodedResponse.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            return@withContext tokenMap["access_token"]
                ?: throw Exception("tokenMap[\"access_token\"] is null")
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun revokeAccessToken(): Boolean {
        return withContext(Dispatchers.IO) {
            val credentials = "${oauthConfig.clientId}:${oauthConfig.clientSecret}"
            val base64Credentials = Base64.encode(credentials.toByteArray()) // 将身份验证信息编码为 Base64
            val accessToken = loginManager.readAccessToken() ?: return@withContext false
            val revokeUrl = "https://api.github.com/applications/${oauthConfig.clientId}/token"
            val requestBody = """{"access_token":"$accessToken"}"""
                .toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(revokeUrl) // URL
                .delete(requestBody)
                .addHeader("Authorization", "Basic $base64Credentials") // 添加 Basic 认证头
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                throw Exception("撤销令牌失败：${response.code}, Response: ${response.body?.string()}")
            }
            fileStore.deleteFile(tokenPath) // 删除 token 文件或执行其他本地清理操作
            return@withContext true
        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun checkAccessToken(): Boolean {
        return withContext(Dispatchers.IO) {
            val credentials = "${oauthConfig.clientId}:${oauthConfig.clientSecret}"
            val base64Credentials = Base64.encode(credentials.toByteArray())
            val accessToken = loginManager.readAccessToken() ?: return@withContext false
            val checkUrl = "https://api.github.com/applications/${oauthConfig.clientId}/token"
            val requestBody = """{"access_token":"$accessToken"}"""
                .toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(checkUrl)
                .post(requestBody)
                .addHeader("Authorization", "Basic $base64Credentials")
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful)
                throw Exception("检查令牌失败：${response.code}, Response: ${response.body?.string()}")
            else(response.code == 200)
                return@withContext true
        }

    }


}