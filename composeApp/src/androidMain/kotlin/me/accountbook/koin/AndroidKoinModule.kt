// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.WebViewManager
import me.accountbook.data.local.AccountHelper
import me.accountbook.data.local.AndroidDatabaseDriverFactory
import me.accountbook.data.local.DatabaseDriverFactory
import me.accountbook.data.local.DatabaseHelper
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.data.local.TagboxHelper
import me.accountbook.data.model.SerAccount
import me.accountbook.data.model.SerTagbox
import me.accountbook.network.utils.AndroidBrowserUtil
import me.accountbook.network.AndroidLoginService
import me.accountbook.network.AndroidUserService
import me.accountbook.network.LoginService
import me.accountbook.network.UserService
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.utils.file.AndroidFileUtil
import me.accountbook.utils.file.FileUtil
import org.koin.dsl.module


val androidModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { InitDatabaseUtil(get()) }
    single { TagboxHelper(get()) }
    single { AccountHelper(get()) }

    single { WebViewManager(get()) }
    single<FileUtil> { AndroidFileUtil(get()) }
    single<BrowserUtil> { AndroidBrowserUtil(get()) }//可以删除
    single<LoginService> { AndroidLoginService }
    single<UserService> { AndroidUserService }
}
