package me.accountbook

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import me.accountbook.network.GitHubApiService
import me.accountbook.network.login.LoginManager
import org.koin.compose.koinInject
import org.koin.core.scope.get

@Composable
fun App() {
    val loginManager:LoginManager = koinInject()
    GitHubApiService.setToken(loginManager.readAccessToken())
}