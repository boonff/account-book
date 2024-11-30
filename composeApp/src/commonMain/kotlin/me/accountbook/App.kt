package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import me.accountbook.network.GitHubApiService
import me.accountbook.network.login.LoginManager
import org.koin.compose.koinInject

@Composable
fun App() {
    val loginManager: LoginManager = koinInject()
    LaunchedEffect(Unit) {
        if (loginManager.checkAccessToken()) {
            GitHubApiService.setToken(loginManager.readAccessToken())
            GitHubApiService.loadUsername()
        }
    }

}