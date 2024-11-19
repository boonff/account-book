package me.accountbook

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import me.accountbook.ui.navigation.Navigator
import me.accountbook.ui.screen.HomeScreen
@Composable
fun App() {
    MaterialTheme {
        Navigator(rememberNavController())
    }
}