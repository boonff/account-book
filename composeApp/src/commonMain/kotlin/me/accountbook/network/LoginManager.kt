package me.accountbook.network

import me.accountbook.koin.OAuthConfig
import okhttp3.OkHttpClient
import java.net.http.HttpClient

interface LoginManager {
    val oauthConfig: OAuthConfig
    val httpClient: OkHttpClient
    val browserScaffold: BrowserScaffold
    fun openLoginPage()
    suspend fun getAccessToken(): String
    fun getAuthorizationUrl(): String

}