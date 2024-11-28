package me.accountbook.network.login

import me.accountbook.koin.OAuthConfig
import okhttp3.OkHttpClient
import java.net.http.HttpClient

interface LoginManager {
    val oauthConfig: OAuthConfig
    fun isLoggedIn(): Boolean

    fun openLoginPage()
    suspend fun saveAccessToken()

    suspend fun revokeAccessToken():Boolean

    suspend fun checkAccessToken():Boolean
    fun readAccessToken(): String?

}