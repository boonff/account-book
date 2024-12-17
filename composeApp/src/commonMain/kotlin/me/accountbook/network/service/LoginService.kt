package me.accountbook.network.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.koin.OAuthConfig
import me.accountbook.koin.getRedirectUri
import me.accountbook.network.utils.ServerUtil
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

abstract class LoginService : KoinComponent {
    private val oauthConfig: OAuthConfig by inject()
    private val httpClient = OkHttpClient()
    protected fun getAuthorizationUrl(): String {
        val baseUrl = oauthConfig.oauthUrl
        val scope = "repo"
        return "$baseUrl?" +
                "client_id=${oauthConfig.clientId}&" +
                "response_type=code&" +
                "redirect_uri=${oauthConfig.getRedirectUri()}&" +
                "scope=$scope&" +
                "prompt=select_account"
    }

    suspend fun getToken(): String? {
        val httpClient = OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))  // 强制使用 HTTP/1.1
            .build()

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
                .addHeader("Cache-Control", "no-cache")
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
                    tokenMap["access_token"]
                } else {
                    println("Error getToken response is failed: ${response.code}")
                    null
                }
            } catch (e: Exception) {
                println("Error getToken: ${e.message}\n")
                null
            } finally {
                ServerUtil.stopServer()
            }
        }
    }


    @OptIn(ExperimentalEncodingApi::class)
    suspend fun revokeToken(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val credentials = "${oauthConfig.clientId}:${oauthConfig.clientSecret}"
            val base64Credentials = Base64.encode(credentials.toByteArray()) // 将身份验证信息编码为 Base64
            val revokeUrl = "https://api.github.com/applications/${oauthConfig.clientId}/token"
            val requestBody = """{"access_token":"$token"}"""
                .toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(revokeUrl) // URL
                .delete(requestBody)
                .addHeader("Authorization", "Basic $base64Credentials") // 添加 Basic 认证头
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            try {
                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Error revokeToken response is failed: ${response.code}")
                    return@withContext false
                }
                return@withContext true
            } catch (e: Exception) {
                println("Error revokeToken:${e.message}")
                false
            }

        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun checkToken(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val credentials = "${oauthConfig.clientId}:${oauthConfig.clientSecret}"
            val base64Credentials = Base64.encode(credentials.toByteArray())
            val checkUrl = "https://api.github.com/applications/${oauthConfig.clientId}/token"
            val requestBody = """{"access_token":"$token"}"""
                .toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(checkUrl)
                .post(requestBody)
                .addHeader("Authorization", "Basic $base64Credentials")
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            try {
                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Error checkToken response is failed:${response.code}")
                    return@withContext false
                } else (response.code == 200)
                return@withContext true
            } catch (e: Exception) {
                println("Error checkToken:${e.message}")
                false
            }

        }

    }
    abstract fun openLoginPage()
}