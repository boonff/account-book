// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.WebViewManager
import me.accountbook.data.local.helper.AccountHelper
import me.accountbook.data.local.AndroidDatabaseDriverFactory
import me.accountbook.data.local.DatabaseDriverFactory
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.data.local.helper.SettingHelper
import me.accountbook.data.local.helper.TableTimestampHelper
import me.accountbook.data.local.helper.TagboxHelper
import me.accountbook.network.utils.AndroidBrowserUtil
import me.accountbook.network.service.AndroidLoginService
import me.accountbook.network.manager.AndroidUserManager
import me.accountbook.network.service.LoginService
import me.accountbook.network.manager.UserManager
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.file.local.AndroidFileUtil
import me.accountbook.file.local.FileUtil
import org.koin.dsl.module

val androidModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { InitDatabaseUtil(get()) }
    single { TagboxHelper(get()) }
    single { AccountHelper(get()) }
    single { TableTimestampHelper(get()) }
    single { SettingHelper(get()) }

    single { WebViewManager(get()) }
    single<FileUtil> { AndroidFileUtil(get()) }
    single<BrowserUtil> { AndroidBrowserUtil(get()) }//可以删除
    single<LoginService> { AndroidLoginService }
    single<UserManager> { AndroidUserManager }
}
