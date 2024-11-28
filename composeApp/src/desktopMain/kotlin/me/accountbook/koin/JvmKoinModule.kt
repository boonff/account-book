package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.JvmDatabaseDriverFactory
import me.accountbook.database.DatabaseDriverFactory
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.network.utils.DeskTopBrowserUtil
import me.accountbook.network.login.DesktopLoginManager
import me.accountbook.network.login.LoginManager
import me.accountbook.network.login.LoginManagerImpl
import me.accountbook.ui.file.DesktopFileUtil
import me.accountbook.utils.file.FileUtil
import org.koin.dsl.module

val jvmModule = module {
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
    single<FileUtil> { DesktopFileUtil() }
    single<BrowserUtil> { DeskTopBrowserUtil() }//可以删除
    single<LoginManager>{ DesktopLoginManager() }
}
