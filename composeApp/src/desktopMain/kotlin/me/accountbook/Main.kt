package me.accountbook

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import me.accountbook.koin.jvmModule
import me.accountbook.ui.theme.AppTheme
import me.accountbook.ui.navigation.Navigator
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin

fun main() = application {
    startKoin {
        modules(jvmModule)
    }

    Window(
        onCloseRequest = {
            // 关闭 Koin 容器
            stopKoin()
            // 退出应用
            exitApplication()
        },
        title = "Account Book",
    ) {
        AppTheme {
            Navigator(rememberNavController())
        }
    }
}
