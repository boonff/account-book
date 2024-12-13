package me.accountbook.data.local

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.model.encode
import me.accountbook.database.Database
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class TagboxHelper(driver: SqlDriver) :
    DatabaseHelper<SerTagbox>(driver) {
    override fun insert(data: SerTagbox): Boolean {
        return try {
            queries.insertTagbox(data.uuid, data.name, data.color)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.insert", e)
            false
        }
    }

    override fun query(): List<SerTagbox>? {
        return try {
            queries.queryAllTagbox().executeAsList().map { it.encode() }
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.query", e)
            null
        }
    }

    override fun queryById(uuid: String): SerTagbox? {
        return try {
            queries.queryTagboxById(uuid).executeAsOneOrNull()?.encode()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.queryById", e)
            null
        }
    }

    override fun update(data: SerTagbox, uuid: String): Boolean {
        return try {
            queries.updateTagboxByUuid(
                data.name,
                data.color,
                data.position,
                data.deleted,
                data.uuid
            )
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.update", e)
            false
        }
    }

    override fun delete(): Boolean {
        return try {
            queries.deleteAllTagbox()
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.delete", e)
            false
        }
    }

    override fun deleteById(uuid: String): Boolean {
        return try {
            queries.deleteTagboxById(uuid)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.DeleteById", e)
            false
        }
    }

    override fun softDelete(uuid: String): Boolean {
        return try {
            queries.softDeleteTagboxById(uuid)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.softDelete", e)
            false
        }
    }

    override fun refactor(dataList: List<SerTagbox>): Boolean {
        return try {
            queries.transaction {
                queries.deleteAllTagbox()
                dataList.forEach {
                    queries.insertTagboxWithAll(
                        it.uuid,
                        it.name,
                        it.color,
                        it.position,
                        it.timestamp,
                        it.deleted
                    )
                }
            }
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("tagbox重构失败", e)
            false
        }
    }


}
