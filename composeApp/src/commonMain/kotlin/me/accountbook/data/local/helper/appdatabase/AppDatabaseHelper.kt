package me.accountbook.data.local.helper.appdatabase

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.local.model.TableKey
import me.accountbook.database.Database

abstract class AppDatabaseHelper<T>(driver: SqlDriver) {
    protected val queries = Database(driver).appDatabaseQueries
    abstract val tableKey: TableKey

    fun <T> runTransaction(block: () -> T) {
        queries.transaction {
            block()
        }
    }

    abstract fun insert(data: T): Boolean
    abstract fun query(): List<T>?
    abstract fun queryUndeleted(): List<T>?
    abstract fun queryById(uuid: String): T?
    abstract fun update(data: T, uuid: String): Boolean
    abstract fun delete(): Boolean
    abstract fun deleteById(uuid: String): Boolean
    abstract fun softDelete(uuid: String): Boolean
    abstract fun hardDelete():Boolean
    abstract fun refactor(dataList: List<T>): Boolean
}
