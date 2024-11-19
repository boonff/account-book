package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.sqldelight.JvmDatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.dsl.module

val jvmModule = module {
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
}
