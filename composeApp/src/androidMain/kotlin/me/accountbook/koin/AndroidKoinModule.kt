// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.WebViewManager
import me.accountbook.data.local.helper.appdatabase.AccountHelper
import me.accountbook.data.local.AndroidDatabaseDriverFactory
import me.accountbook.data.local.factory.DatabaseDriverFactory
import me.accountbook.data.local.helper.InitDatabaseUtil
import me.accountbook.data.local.helper.keyValueStore.SettingHelper
import me.accountbook.data.local.helper.keyValueStore.TableTimestampHelper
import me.accountbook.data.local.helper.appdatabase.TagboxHelper
import me.accountbook.network.utils.AndroidBrowserUtil
import me.accountbook.network.service.AndroidLoginService
import me.accountbook.network.manager.AndroidUserManager
import me.accountbook.network.service.LoginService
import me.accountbook.network.manager.UserManager
import me.accountbook.network.utils.BrowserUtil
import me.accountbook.file.local.AndroidFileUtil
import me.accountbook.file.local.FileUtil
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
import me.accountbook.ui.setting.viewmodel.AndroidLoginViewModel
import org.koin.core.module.dsl.viewModel
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

    viewModel<LoginViewModel> { AndroidLoginViewModel() }
}
