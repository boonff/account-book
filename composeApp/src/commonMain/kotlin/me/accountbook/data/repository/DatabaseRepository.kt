package me.accountbook.data.repository

import me.accountbook.data.local.DatabaseHelper
import me.accountbook.data.model.SerDataItem
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.SQLException

open class DatabaseRepository<T : SerDataItem>(private val dbHelper: DatabaseHelper<T>) :
    KoinComponent {
    private fun config() {

    }

    protected fun updateProperty(
        uuid: String,
        changeProperty: (table: T) -> T
    ): Boolean {
        val table = dbHelper.queryById(uuid)
        table?.let {
            val updatedTable = changeProperty(it)
            dbHelper.update(updatedTable, uuid)
            config()
            return true
        } ?: run {
            LoggingUtil.logDebug("queryById获取到的值为空")
        }
        return false
    }

    fun insert(data: T): Boolean {
        return dbHelper.insert(data).also { if (it) config() }
    }

    fun clear(): Boolean {
        return dbHelper.delete().also { if (it) config() }
    }

    fun softDelete(uuid: String): Boolean {
        return dbHelper.softDelete(uuid).also { if (it) config() }
    }

    fun query(): List<T>? {
        return dbHelper.query()
    }

    fun refactor(merged: List<T>): Boolean {
        return dbHelper.refactor(merged)
    }
}