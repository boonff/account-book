// AndroidKoinModule.kt (Android)
package me.accountbook.koin

import android.content.Context
import me.accountbook.sqldelight.AndroidDatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseDriverFactory
import org.koin.dsl.module

val androidModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
}
