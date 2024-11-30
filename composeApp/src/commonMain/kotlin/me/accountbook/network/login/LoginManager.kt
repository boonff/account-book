package me.accountbook.network.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.accountbook.koin.OAuthConfig
import me.accountbook.koin.getRedirectUri
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.FileUtil
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

abstract class LoginManager : KoinComponent {
    private val oauthConfig: OAuthConfig by inject()
    protected val fileStore: FileUtil by inject()
    private val loginManager: LoginManager by inject()

    protected val tokenPath: String = "token"
    private val httpClient = OkHttpClient()

    fun isLoggedIn(): Boolean {
        return fileStore.isFileExist(tokenPath)
    }

    protected fun getAuthorizationUrl(): String {
        val baseUrl = oauthConfig.oauthUrl
        val scope = "repo"
        return "$baseUrl?client_id=${oauthConfig.clientId}&response_type=code&redirect_uri=${oauthConfig.getRedirectUri()}&scope=$scope"//scope需要填写
    }

    protected suspend fun getAccessToken(): String? {
        val authorizationCode = ServerUtil.getAuthorizationCode()
        authorizationCode ?: return null


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
            try {
                val response = httpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val decodedResponse =
                        URLDecoder.decode(responseBody, StandardCharsets.UTF_8.name())
                    val tokenMap = decodedResponse.split("&").associate {
                        val (key, value) = it.split("=")
                        key to value
                    }
                    ServerUtil.stopServer()
                    tokenMap["access_token"]
                } else {
                    println("Failed to retrieve access token. HTTP status: ${response.code}")
                    null
                }
            } catch (e: Exception) {
                println("Error making HTTP request for access token: ${e.message}")
                null
            }
        }
    }


    @OptIn(ExperimentalEncodingApi::class)
    suspend fun revokeAccessToken(): Boolean {
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
                return@withContext false
            }
            fileStore.deleteFile(tokenPath) // 删除 token 文件或执行其他本地清理操作
            return@withContext true
        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun checkAccessToken(): Boolean {
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
                return@withContext false
            else (response.code == 200)
            return@withContext true
        }

    }


    abstract fun openLoginPage()

    abstract suspend fun saveAccessToken(): Boolean

    abstract suspend fun deleteAccessToken(): Boolean

    abstract fun readAccessToken(): String?


}