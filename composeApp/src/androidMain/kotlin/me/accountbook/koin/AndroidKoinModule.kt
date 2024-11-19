// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import me.accountbook.sqldelight.AndroidDatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.dsl.module


val androidModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<DatabaseHelper> { DatabaseHelper(get()) }
}
