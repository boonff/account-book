package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.local.helper.AccountHelper
import me.accountbook.data.local.JvmDatabaseDriverFactory
import me.accountbook.data.local.DatabaseDriverFactory
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.data.local.helper.SettingHelper
import me.accountbook.data.local.helper.TableTimestampHelper
import me.accountbook.data.local.helper.TagboxHelper
import me.accountbook.network.manager.DesktopUserManager
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.network.utils.DeskTopBrowserUtil
import me.accountbook.network.service.DesktopLoginService
import me.accountbook.network.service.LoginService
import me.accountbook.network.manager.UserManager
import me.accountbook.file.local.DesktopFileUtil
import me.accountbook.file.local.FileUtil
import org.koin.dsl.module

val jvmModule = module {
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single { InitDatabaseUtil(get()) }
    single { TagboxHelper(get()) }
    single { AccountHelper(get()) }
    single { TableTimestampHelper(get()) }
    single { SettingHelper(get()) }

    single<FileUtil> { DesktopFileUtil() }
    single<BrowserUtil> { DeskTopBrowserUtil() }//可以删除
    single<LoginService> { DesktopLoginService }
    single<UserManager> { DesktopUserManager }
}
