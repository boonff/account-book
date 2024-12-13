package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import me.accountbook.data.local.DatabaseHelper
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.network.UserService
import org.koin.compose.koinInject

@Composable
fun App() {
    val userService: UserService = koinInject()
    val initDB: InitDatabaseUtil = koinInject()
    initDB.initDatabase()
    LaunchedEffect(Dispatchers.IO) {
        userService.initUser()
    }

}