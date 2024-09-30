package me.accountbook

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import me.accountbook.ui.navigation.Navigator

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Account Book",
    ) {
        AppTheme {
            Navigator()
        }

    }
}