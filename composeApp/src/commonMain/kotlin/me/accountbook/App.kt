package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.UserService
import org.koin.compose.koinInject

@Composable
fun App() {
    val userService:UserService = koinInject()
    val dbHelper:DatabaseHelper = koinInject()
    dbHelper.initDatabase()
    LaunchedEffect(Dispatchers.IO) {
        userService.initUser()
    }

}