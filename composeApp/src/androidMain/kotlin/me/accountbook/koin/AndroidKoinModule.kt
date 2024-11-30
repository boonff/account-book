// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.WebViewManager
import me.accountbook.database.AndroidDatabaseDriverFactory
import me.accountbook.database.DatabaseDriverFactory
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.utils.AndroidBrowserUtil
import me.accountbook.network.login.AndroidLoginManager
import me.accountbook.network.login.LoginManager
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.utils.file.AndroidFileUtil
import me.accountbook.utils.file.FileUtil
import org.koin.dsl.module


val androidModule = module {
    single { WebViewManager(get()) }
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
    single<FileUtil> { AndroidFileUtil(get()) }
    single<BrowserUtil> { AndroidBrowserUtil(get()) }//可以删除
    single<LoginManager> { AndroidLoginManager }
}
