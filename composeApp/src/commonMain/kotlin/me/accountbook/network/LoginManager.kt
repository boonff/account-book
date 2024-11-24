package me.accountbook.network

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URI


class LoginManager(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String
): KoinComponent {
    private val httpClient = OkHttpClient()
    private val serverManager = ServerManager()
    private val networkScaffold:NetworkScaffold by inject()

    // 用来构建 OAuth2 授权请求的 URL
    private fun getAuthorizationUrl(): String {
        return "https://github.com/login/oauth/authorize?client_id=$clientId&response_type=code&redirect_uri=$redirectUri&scope=files.read"
    }

    // 打开浏览器进行登录授权

    fun openLoginPage() {
        val authorizationUrl = getAuthorizationUrl()
        val uri = URI.create(authorizationUrl)

        serverManager.startServer()//启动回调服务器监听授权码
        networkScaffold.openBrowser(uri.toString())
    }

    // 用授权码请求访问令牌
    suspend fun getAccessToken(): String {
        try {
            val authorizationCode = serverManager.getAuthorizationCode()

            val requestBody = FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", authorizationCode) // 获取授权码
                .add("redirect_uri", redirectUri)
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .build()

            val request = Request.Builder()
                .url("https://github.com/login/oauth/access_token")//这里需要解耦
                .post(requestBody)
                .build()
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                // 解析响应体中的访问令牌
                val jsonResponse = response.body!!.string()//response在极端情况下可能为空？
                // 提取访问令牌（这里只是示例，实际解析可能需要使用 JSON 库）
                return parseAccessTokenFromJson(jsonResponse)
                    ?: throw Exception("Failed to parse access token from response: $jsonResponse")
            } else {
                throw Exception("Token request failed: ${response.code}")
            }
        } catch (e: Exception) {
            println("Error requesting access token: ${e.message}")
            throw e
        }
    }


    // 解析访问令牌（这里假设返回的 JSON 中有 access_token 字段）
    private fun parseAccessTokenFromJson(jsonResponse: String?): String? {
        // 简单示例，使用正则从 JSON 中提取 access_token
        return jsonResponse?.substringAfter("\"access_token\":\"")?.substringBefore("\"")
    }
}
