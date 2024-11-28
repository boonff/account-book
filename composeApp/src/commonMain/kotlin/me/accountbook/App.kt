package me.accountbook

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.withContext
import me.accountbook.network.GitHubApiService
import me.accountbook.network.login.LoginManager
import org.koin.compose.koinInject
import org.koin.core.scope.get

@Composable
fun App() {
    val loginManager:LoginManager = koinInject()
    LaunchedEffect(Unit){
        if(!loginManager.checkAccessToken())
            loginManager.saveAccessToken()
        GitHubApiService.setToken(loginManager.readAccessToken())
    }

}