package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.database.JvmDatabaseDriverFactory
import me.accountbook.database.DatabaseDriverFactory
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.NetworkScaffold
import me.accountbook.network.DeskTopNetworkScaffold
import me.accountbook.platform.DesktopFileStorage
import me.accountbook.platform.FileStorage
import org.koin.dsl.module

val jvmModule = module {
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
    single<FileStorage> { DesktopFileStorage() }
    single<NetworkScaffold> {DeskTopNetworkScaffold()  }
}
