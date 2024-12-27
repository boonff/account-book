package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.local.helper.appdatabase.AccountHelper
import me.accountbook.data.local.JvmDatabaseDriverFactory
import me.accountbook.data.local.factory.DatabaseDriverFactory
import me.accountbook.data.local.helper.InitDatabaseUtil
import me.accountbook.data.local.helper.keyValueStore.SettingHelper
import me.accountbook.data.local.helper.keyValueStore.TableTimestampHelper
import me.accountbook.data.local.helper.appdatabase.TagboxHelper
import me.accountbook.network.manager.DesktopUserManager
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.network.utils.DeskTopBrowserUtil
import me.accountbook.network.service.DesktopLoginService
import me.accountbook.network.service.LoginService
import me.accountbook.network.manager.UserManager
import me.accountbook.file.local.DesktopFileUtil
import me.accountbook.file.local.FileUtil
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
import me.accountbook.ui.setting.viewmodel.JvmLoginViewModel
import org.koin.core.module.dsl.viewModel
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

    viewModel<LoginViewModel>{ JvmLoginViewModel() }
}
