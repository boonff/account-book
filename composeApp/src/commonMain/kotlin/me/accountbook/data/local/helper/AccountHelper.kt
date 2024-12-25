package me.accountbook.data.local.helper

import app.cash.sqldelight.db.SqlDriver
import me.accountbook.data.model.TableKey
import me.accountbook.database.Account
import me.accountbook.utils.LoggingUtil
import java.sql.SQLException

class AccountHelper(driver: SqlDriver) :
    AppDatabaseHelper<Account>(driver) {
    override val tableKey = TableKey.Account
    override fun insert(data: Account): Boolean {
        return try {
            queries.insertAccount(
                data.uuid,
                data.name,
                data.balance,
                data.targetSavings,
                data.emergencySavings,
                data.position
            )
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.insert", e)
            false
        }
    }

    override fun query(): List<Account>? {
        return try {
            queries.queryAllAccount().executeAsList()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.query", e)
            null
        }
    }

    override fun queryUndeleted(): List<Account>? {
        return try {
            queries.queryUndeletedAccount().executeAsList()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.query", e)
            null
        }
    }

    override fun queryById(uuid: String): Account? {
        return try {
            queries.queryAccountById(uuid).executeAsOneOrNull()
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.queryById", e)
            null
        }
    }

    override fun delete(): Boolean {
        return try {
            queries.deleteAllAccount()
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.delete", e)
            false
        }
    }

    override fun deleteById(uuid: String): Boolean {
        return try {
            queries.deleteAccountById(uuid)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.deleteById", e)
            false
        }
    }

    override fun softDelete(uuid: String): Boolean {
        return try {
            queries.softDeleteAccount(uuid)
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.softDelete", e)
            false
        }
    }

    override fun hardDelete(): Boolean {
        return try {
            queries.hardDeleteTagbox()
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("account 硬删除失败", e)
            false
        }
    }

    override fun refactor(dataList: List<Account>): Boolean {
        return try {
            queries.transaction {
                queries.deleteAllAccount()
                dataList.forEach {
                    queries.insertAccountWithAll(
                        it.uuid,
                        it.timestamp,
                        it.deleted,
                        it.name,
                        it.balance,
                        it.targetSavings,
                        it.emergencySavings,
                        it.position
                    )
                }
            }
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("account重构失败", e)
            false
        }
    }

    override fun update(data: Account, uuid: String): Boolean {
        return try {
            queries.updateAccountByUuid(
                data.name,
                data.balance,
                data.targetSavings,
                data.emergencySavings,
                data.deleted,
                data.uuid
            )
            true
        } catch (e: SQLException) {
            LoggingUtil.logError("fun: AccountDatabaseHelper.update", e)
            false
        }
    }

}
