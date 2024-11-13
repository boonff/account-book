package me.accountbook.ui.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen {
    data object HomeScreen : Screen()
    data object TranscationScreen : Screen()
    data object SettingScreen: Screen()


}

val ScreenSaver: Saver<Screen, String> = Saver(
    save = { screen ->
        when (screen) {
            is Screen.HomeScreen -> "home"
            is Screen.TranscationScreen -> "transaction"
            is Screen.SettingScreen -> "settings"
        }
    },
    restore = { value ->
        when (value) {
            "home" -> Screen.HomeScreen
            "transaction" -> Screen.TranscationScreen
            "settings" -> Screen.SettingScreen
            else -> Screen.HomeScreen // 默认返回
        }
    }
)