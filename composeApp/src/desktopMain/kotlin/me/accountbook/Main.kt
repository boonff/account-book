package me.accountbook

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.json.Json
import me.accountbook.koin.commonModule
import me.accountbook.koin.jvmModule
import me.accountbook.database.DatabaseHelper
import me.accountbook.ui.theme.AppTheme
import me.accountbook.ui.navigation.Navigator
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.component.inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
class MyApp : KoinComponent {
    // 在此进行 Koin 的注入
    private val dbHelper: DatabaseHelper by inject()
    fun initializeApp() {
        // 使用注入的 dbHelper 初始化数据库
        dbHelper.initializeDatabase()
    }
}

fun main() = application {
    //依赖注入相关
    startKoin {
        modules(jvmModule, commonModule)
    }
    val app = MyApp()
    app.initializeApp()
    Window(
        icon =  painterResource("icon_wallet.png"),
        onCloseRequest = {
            // 关闭 Koin 容器
            stopKoin()
            // 退出应用
            exitApplication()
        }
    ) {
        App()
        AppTheme {
            Navigator(rememberNavController())
        }
    }
}

