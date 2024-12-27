package me.accountbook.data.local


import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.accountbook.data.local.factory.DatabaseDriverFactory
import me.accountbook.database.Database

// JvmDatabaseDriverFactory.kt
class JvmDatabaseDriverFactory() : DatabaseDriverFactory {
    private val jdbcUrl = "jdbc:sqlite:AppDatabase.db" // 保存 JDBC URL

    // 使用连接池获取 SqlDriver
    override fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(jdbcUrl) // 使用保存的 JDBC URL
        Database.Schema.create(driver) // 创建数据库模式（如果尚未创建）
        return driver
    }

}
