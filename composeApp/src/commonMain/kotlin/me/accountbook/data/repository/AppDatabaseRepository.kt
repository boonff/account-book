package me.accountbook.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.local.helper.AppDatabaseHelper
import me.accountbook.utils.LoggingUtil

open class AppDatabaseRepository<T>(private val dbHelper: AppDatabaseHelper<T>) {
    val tableKey = dbHelper.tableKey

    // 更新属性
    suspend fun updateProperty(
        uuid: String,
        changeProperty: (table: T) -> T
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val table = dbHelper.queryById(uuid) ?: run {
                LoggingUtil.logDebug("queryById获取到的值为空")
                return@withContext false
            }
            val updatedTable = changeProperty(table)
            dbHelper.update(updatedTable, uuid)
            true
        }
    }

    // 插入数据
    suspend fun insert(data: T): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.insert(data)
        }
    }

    // 清空数据
    suspend fun clear(): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.delete()
        }
    }

    // 执行软删除
    suspend fun softDelete(uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.softDelete(uuid)
        }
    }
    suspend fun hardDelete():Boolean{
        return withContext(Dispatchers.IO){
            dbHelper.hardDelete()
        }
    }
    // 查询所有数据
    suspend fun query(): List<T>? {
        return withContext(Dispatchers.IO) {
            dbHelper.query()
        }
    }

    // 查询未删除的数据
    suspend fun queryUndeleted(): List<T>? {
        return withContext(Dispatchers.IO) {
            dbHelper.queryUndeleted()
        }
    }



    // 重构数据
    suspend fun refactor(merged: List<T>): Boolean {
        return withContext(Dispatchers.IO) {
            dbHelper.refactor(merged)
        }
    }
}
