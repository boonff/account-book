package me.accountbook.data.repository

import me.accountbook.data.local.helper.AppDatabaseHelper
import me.accountbook.utils.LoggingUtil

open class AppDatabaseRepository<T>(private val dbHelper: AppDatabaseHelper<T>) {
    val tableKey = dbHelper.tableKey

    protected fun updateProperty(
        uuid: String,
        changeProperty: (table: T) -> T
    ): Boolean {
        val table = dbHelper.queryById(uuid) ?: run {
            LoggingUtil.logDebug("queryById获取到的值为空")
            return false
        }
        val updatedTable = changeProperty(table)
        dbHelper.update(updatedTable, uuid)
        return true
    }

    fun insert(data: T): Boolean {
        return dbHelper.insert(data)
    }

    fun clear(): Boolean {
        return dbHelper.delete()
    }

    fun softDelete(uuid: String): Boolean {
        return dbHelper.softDelete(uuid)
    }

    fun query(): List<T>? {
        return dbHelper.query()
    }

    fun queryUndeleted(): List<T>? {
        return dbHelper.queryUndeleted()
    }

    fun refactor(merged: List<T>): Boolean {
        return dbHelper.refactor(merged)
    }
}