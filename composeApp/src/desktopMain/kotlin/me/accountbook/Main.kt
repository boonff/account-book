package me.accountbook

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import me.accountbook.koin.commonModule
import me.accountbook.koin.jvmModule
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.theme.AppTheme
import me.accountbook.ui.navigation.Navigator
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.component.inject

class MyApp : KoinComponent {
    // 在此进行 Koin 的注入
    private val dbHelper: DatabaseHelper by inject()

    fun initializeApp() {
        // 使用注入的 dbHelper 初始化数据库
        dbHelper.initializeDatabase()
    }
}

fun main() = application {
    startKoin {
        modules(jvmModule, commonModule)
    }
    // 创建 MyApp 实例，并初始化应用
    val app = MyApp()
    app.initializeApp()
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
