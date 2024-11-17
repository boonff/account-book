package me.accountbook.koin

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.sqldelight.JvmDatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.dsl.module

val jvmModule = module {

    // 注册 DatabaseDriverFactory
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    // 注册 SqlDriver，使用 Factory 的 createDriver 方法生成
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    // 注册 DatabaseHelper，依赖 SqlDriver
    single<DatabaseHelper> { DatabaseHelper(get()) }
}
