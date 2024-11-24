// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.AndroidDatabaseDriverFactory
import me.accountbook.database.DatabaseDriverFactory
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.AndroidNetworkScaffold
import me.accountbook.network.NetworkScaffold
import me.accountbook.platform.AndroidFileStorage
import me.accountbook.platform.FileStorage
import org.koin.dsl.module


val androidModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
    single<FileStorage> { AndroidFileStorage(get()) }
    single<NetworkScaffold> { AndroidNetworkScaffold(get()) }
}
