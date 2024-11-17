package me.accountbook.sqldelight


import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.accountbook.database.Database

// JvmDatabaseDriverFactory.kt
class JvmDatabaseDriverFactory : DatabaseDriverFactory {

    private val jdbcUrl = "jdbc:sqlite:AppDatabase.db" // 保存 JDBC URL

    // 创建 HikariDataSource
    private val dataSource: HikariDataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = this@JvmDatabaseDriverFactory.jdbcUrl // 配置 SQLite 数据库连接
            maximumPoolSize = 5 // 最大连接池大小
            minimumIdle = 1 // 最小空闲连接
            idleTimeout = 30000 // 空闲连接超时时间，单位毫秒
            connectionTimeout = 30000 // 连接超时时间，单位毫秒
        }
        HikariDataSource(config) // 创建数据源
    }

    // 使用连接池获取 SqlDriver
    override fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(jdbcUrl) // 使用保存的 JDBC URL
        Database.Schema.create(driver) // 创建数据库模式（如果尚未创建）
        return driver
    }

    // 关闭数据源
    fun close() {
        dataSource.close() // 关闭连接池
    }
}
