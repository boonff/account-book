package me.accountbook.network

interface NetworkScaffold {
    fun openBrowser(url: String)
    fun saveAccessToken(token: String)
    fun getAccessToken(): String
}