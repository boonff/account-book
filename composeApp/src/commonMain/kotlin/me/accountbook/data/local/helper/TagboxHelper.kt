package me.accountbook.data.local.helper

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.model.TableKey
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class TagboxHelper(driver: SqlDriver) :
    AppDatabaseHelper<Tagbox>(driver) {
    override val tableKey = TableKey.Tagbox
    override fun insert(data: Tagbox): Boolean {
        return try {
            queries.insertTagbox(data.uuid, data.name, data.color, data.position)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.insert", e)
            false
        }
    }

    override fun query(): List<Tagbox>? {
        return try {
            queries.queryAllTagbox().executeAsList()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.query", e)
            null
        }
    }

    override fun queryUndeleted(): List<Tagbox>? {
        return try {
            queries.queryUndeletedTagbox().executeAsList()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.query", e)
            null
        }
    }

    override fun queryById(uuid: String): Tagbox? {
        return try {
            queries.queryTagboxById(uuid).executeAsOneOrNull()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: TagboxDatabaseHelper.queryById", e)
            null
        }
    }

    override fun update(data: Tagbox, uuid: String): Boolean {
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

    override fun hardDelete(): Boolean {
        return try {
            queries.hardDeleteTagbox()
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("tagbox 硬删除失败", e)
            false
        }
    }

    override fun refactor(dataList: List<Tagbox>): Boolean {
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
