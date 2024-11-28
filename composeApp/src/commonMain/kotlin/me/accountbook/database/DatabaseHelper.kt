package me.accountbook.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.sqldelight.db.SqlDriver
import me.accountbook.utils.SyncUtil
import me.accountbook.utils.serialization.SerializableDatabase
import me.accountbook.utils.serialization.SerializableTagbox
import java.sql.SQLException
import java.util.UUID


//需要在添加和修改数据库的方法上使用 setNoSynced()
class DatabaseHelper(private val driver: SqlDriver) {

    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    private fun setNoSynced() {
        SyncUtil.setNoSynced()
    }

    fun initializeDatabase() {

        queries.dropSetPositionTrigger()

        queries.createSetPositionTrigger()
        queries.createTagboxTimstampTragger()
    }

    // tagbox表的方法
    fun insertTagBox(name: String, color: Color) {
        try {
            queries.insertTagbox(
                UUID.randomUUID().toString(),
                name,
                color.toArgb().toUInt().toLong(),
            )
            setNoSynced()

        } catch (e: SQLException) {
            println("Error inserting tag box: ${e.message}")
            throw e
        }
    }

    fun insertTagBox(tagbox: Tagbox) {
        try {
            queries.insertTagboxWithTimetamp(
                tagbox.uuid,
                tagbox.name,
                tagbox.color,
                tagbox.position,
                tagbox.timestamp,
                tagbox.deleted,
            )
            setNoSynced()
        } catch (e: SQLException) {
            println("Error inserting tagbox: ${e.message}")
        }
    }

    fun insertTagBox(tagbox: SerializableTagbox) {
        try {
            queries.insertTagboxWithTimetamp(
                tagbox.uuid,
                tagbox.name,
                tagbox.color,
                tagbox.position,
                tagbox.timestamp,
                tagbox.deleted,
            )
            setNoSynced()
        } catch (e: SQLException) {
            println("Error inserting tagbox: ${e.message}")
        }
    }

    fun queryUndeletedTagBox(): List<Tagbox> {
        return try {
            queries.queryUndeletedTagbox().executeAsList()
        } catch (e: SQLException) {
            println("Error querying undeleted tag boxes: ${e.message}")
            emptyList()
        }
    }

    fun queryAllTagbox(): List<Tagbox> {
        return try {
            queries.queryAllTagbox().executeAsList()
        } catch (e: SQLException) {
            println("Error querying all tagbox: ${e.message}")
            emptyList()
        }
    }

    fun updateTagboxName(name: String, uuid: String) {
        try {
            queries.updateTagboxName(name, uuid)
            setNoSynced()

        } catch (e: SQLException) {
            println("Error updateTagboxName:${e.message}")
        }
    }

    fun updateTagboxColor(color: Color, uuid: String) {
        try {
            queries.updateTagboxColor(color.toArgb().toUInt().toLong(), uuid)
            setNoSynced()

        } catch (e: SQLException) {
            println("Error updateTagboxColor${e.message}")
        }
    }

    fun updateTagboxPosition(position: Int, uuid: String) {
        try {
            queries.updateTagboxPosition(position.toLong(), uuid)
            setNoSynced()

        } catch (e: SQLException) {
            println("Error updateTagboxPosition: ${e.message}")
        }
    }

    fun updateTagbox(tagbox: Tagbox) {
        try {
            queries.updateTagboxById(
                tagbox.name,
                tagbox.color,
                tagbox.position,
                tagbox.deleted,
                tagbox.uuid
            )
            setNoSynced()
        } catch (e: SQLException) {
            println("Error updateTagbox: ${e.message}")
        }
    }

    fun updateTagbox(tagbox: SerializableTagbox) {
        try {
            queries.updateTagboxByUuid(
                tagbox.name,
                tagbox.color,
                tagbox.position,
                tagbox.deleted,
                tagbox.uuid
            )
            setNoSynced()
        } catch (e: SQLException) {
            println("Error updateTagbox: ${e.message}")
        }
    }

    fun softDeleteTagbox(uuid: String) {
        try {
            queries.softDeleteTagbox(uuid)
            setNoSynced()
        } catch (e: SQLException) {
            println("Error softDeleteTagbox")
        }
    }

    fun deleteAllTagbox() {
        try {
            queries.deleteAllTagbox()
            setNoSynced()
        } catch (e: SQLException) {
            println("Error delete all tagbox :${e.message}")
        }
    }

    fun deleteAllDeletedTagbox(){
        try{
            queries.deleteAllDeletedTagbox()
            setNoSynced()
        }catch (e:SQLException){
            println("Error delete all deleted tagbox: ${e.message}")
        }
    }

    fun refactorDatabase(mergedDB: SerializableDatabase) {
        queries.transaction {
            deleteAllTagbox()
            mergedDB.serializableTagboxs.forEach {
                insertTagBox(it)
            }
        }

    }

    // 账户表的方法
    fun queryAllAccount(): List<Account> {
        return try {
            queries.queryAllAccount().executeAsList()

        } catch (e: SQLException) {
            println("Error querying all accounts: ${e.message}")
            emptyList() // 返回空列表，避免崩溃
        }
    }
}
