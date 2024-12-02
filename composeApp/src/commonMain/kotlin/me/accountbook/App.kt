package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import me.accountbook.network.GitHubApiService
import me.accountbook.network.LoginService
import me.accountbook.network.UserService
import org.koin.compose.koinInject

@Composable
fun App() {
    val userService:UserService = koinInject()
    LaunchedEffect(Unit) {
        userService.initUser()
    }

}