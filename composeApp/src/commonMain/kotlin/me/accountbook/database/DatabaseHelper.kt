package me.accountbook.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.sqldelight.db.SqlDriver
import me.accountbook.utils.SyncUtil
import me.accountbook.utils.serialization.SerializableAccount
import me.accountbook.utils.serialization.SerializableDatabase
import me.accountbook.utils.serialization.SerializableTagbox
import java.sql.SQLException
import java.util.UUID


//需要在添加和修改数据库的方法上使用 setNoSynced()
class DatabaseHelper(private val driver: SqlDriver) {

    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    private fun setNotSynced() {
        SyncUtil.setNotSynced()
    }

    fun initDatabase() {
        queries.createTagbox()
        queries.createAccount()

        queries.dropSetPositionTrigger()
        queries.dropAccountTimestampTrigger()

        queries.createSetPositionTrigger()
        queries.createTagboxTimstampTragger()
        queries.createAccountTimestampTrigger()
    }

    // tagbox表的方法
    fun insertTagBox(name: String, color: Color) {
        try {
            queries.insertTagbox(
                UUID.randomUUID().toString(),
                name,
                color.toArgb().toUInt().toLong(),
            )
            setNotSynced()

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
            setNotSynced()
        } catch (e: SQLException) {
            println("Error inserting tagbox: ${e.message}")
        }
    }

    //插入序列化类时必须指定时间戳
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
            setNotSynced()
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
            setNotSynced()

        } catch (e: SQLException) {
            println("Error updateTagboxName:${e.message}")
        }
    }

    fun updateTagboxColor(color: Color, uuid: String) {
        try {
            queries.updateTagboxColor(color.toArgb().toUInt().toLong(), uuid)
            setNotSynced()

        } catch (e: SQLException) {
            println("Error updateTagboxColor${e.message}")
        }
    }

    fun updateTagboxPosition(position: Int, uuid: String) {
        try {
            queries.updateTagboxPosition(position.toLong(), uuid)
            setNotSynced()

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
            setNotSynced()
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
            setNotSynced()
        } catch (e: SQLException) {
            println("Error updateTagbox: ${e.message}")
        }
    }

    fun softDeleteTagbox(uuid: String) {
        try {
            queries.softDeleteTagbox(uuid)
            setNotSynced()
        } catch (e: SQLException) {
            println("Error softDeleteTagbox")
        }
    }

    fun deleteAllTagbox() {
        try {
            queries.deleteAllTagbox()
            setNotSynced()
        } catch (e: SQLException) {
            println("Error delete all tagbox :${e.message}")
        }
    }

    fun deleteAllDeletedTagbox() {
        try {
            queries.deleteAllDeletedTagbox()
            setNotSynced()
        } catch (e: SQLException) {
            println("Error delete all deleted tagbox: ${e.message}")
        }
    }

    fun refactorDatabase(mergedDB: SerializableDatabase) {
        queries.transaction {
            deleteAllTagbox()
            deleteAllAccount()
            mergedDB.serializableTagboxs.forEach {
                insertTagBox(it)
            }
            mergedDB.serializableAccount.forEach{
                insertAccount(it)
            }
        }

    }

    // 账户表的方法
    fun insertAccount(name: String, balance: Double, minBalance: Double) {
        try {
            queries.insertAccount(
                UUID.randomUUID().toString(),
                name,
                balance,
                minBalance,
            )
            setNotSynced()
        } catch (e: SQLException) {
            println("Error inserting account: ${e.message}")
        }
    }

    fun insertAccount(account: SerializableAccount) {
        try {
            queries.insertAccountWithAll(
                account.uuid,
                account.deleted,
                account.timestamp,
                account.name,
                account.balance,
                account.minBalance,
            )
            setNotSynced()
        } catch (e: SQLException) {
            println("Error inserting account: ${e.message}")
        }
    }

    fun queryAllAccount(): List<Account> {
        return try {
            queries.queryAllAccount().executeAsList()
        } catch (e: SQLException) {
            println("Error querying all accounts: ${e.message}")
            emptyList()
        }
    }

    fun updateAccountBalance(balance: Double, uuid: String) {
        try {
            queries.updateAccountBalance(balance, uuid)
            setNotSynced()
        } catch (e: SQLException) {
            println("Error updating account balance: ${e.message}")
        }
    }

    fun updateAccountMinBalance(minBalance: Double, uuid: String) {
        try {
            queries.updateAccountMinBalance(minBalance, uuid)
            setNotSynced()
        } catch (e: SQLException) {
            println("Error updating account min balance: ${e.message}")
        }
    }

    fun updateAccount(account: Account) {
        try {
            queries.updateAccountByUuid(
                account.name,
                account.balance,
                account.minBalance,
                account.deleted,
                account.uuid
            )
            setNotSynced()
        } catch (e: SQLException) {
            println("Error updating account: ${e.message}")
        }
    }

    fun softDeleteAccount(uuid: String) {
        try {
            queries.softDeleteAccount(uuid)
            setNotSynced()
        } catch (e: SQLException) {
            println("Error soft deleting account: ${e.message}")
        }
    }

    fun recoverAccount(uuid: String) {
        try {
            queries.recoverAccount(uuid)
            setNotSynced()
        } catch (e: SQLException) {
            println("Error recovering account: ${e.message}")
        }
    }

    fun deleteAllAccount() {
        try {
            queries.deleteAllAccount()
            setNotSynced()
        } catch (e: SQLException) {
            println("Error deleting all accounts: ${e.message}")
        }
    }

    fun deleteAllDeletedAccount() {
        try {
            queries.deleteAllDeletedAccount()
            setNotSynced()
        } catch (e: SQLException) {
            println("Error deleting all deleted accounts: ${e.message}")
        }
    }


}
