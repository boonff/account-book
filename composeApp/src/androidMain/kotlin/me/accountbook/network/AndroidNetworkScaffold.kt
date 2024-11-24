package me.accountbook.network

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidNetworkScaffold(private val context: Context) : NetworkScaffold {
    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)  // 使用 context 来启动
    }

    override fun saveAccessToken(token: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    override fun getAccessToken(): String {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
            ?: throw Exception("access_token is null")
    }
}