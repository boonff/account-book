package me.accountbook

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import me.accountbook.koin.commonModule
import me.accountbook.koin.jvmModule
import me.accountbook.ui.theme.AppTheme
import me.accountbook.ui.navigation.Navigator
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin

fun main() = application {
    //依赖注入相关
    startKoin {
        modules(jvmModule, commonModule)
    }
    Window(
        icon =  painterResource("icon_wallet.png"),
        onCloseRequest = {
            stopKoin()
            exitApplication()
        }
    ) {
        AppInitializer.initialize()
        AppTheme {
            Navigator(rememberNavController())
        }
    }
}

