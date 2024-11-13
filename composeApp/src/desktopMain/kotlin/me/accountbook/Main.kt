package me.accountbook

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.accountbook.ui.theme.AppTheme
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