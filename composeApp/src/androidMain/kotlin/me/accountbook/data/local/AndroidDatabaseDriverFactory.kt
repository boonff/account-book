package me.accountbook.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.accountbook.data.local.factory.DatabaseDriverFactory
import me.accountbook.database.Database

class AndroidDatabaseDriverFactory(private val context:Context): DatabaseDriverFactory {
    override fun createDriver():SqlDriver{
        return AndroidSqliteDriver(Database.Schema, context, "AppDatabase.dp")
    }
}