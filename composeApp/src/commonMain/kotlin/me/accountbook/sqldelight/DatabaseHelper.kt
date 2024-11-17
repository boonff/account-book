package me.accountbook.sqldelight

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.database.Account
import me.accountbook.database.Bill
import me.accountbook.database.Database
import me.accountbook.database.Envelope
import me.accountbook.database.User

class DatabaseHelper(private val driver: SqlDriver) {

    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    // ---- User 表操作 ----

    // 插入用户
    fun close(){
        driver.close()
    }
    suspend fun insertUser(username: String, email: String?, password_hash: String, created_at: String) {
        withContext(Dispatchers.IO) {
            queries.insertUser(username, email, password_hash, created_at)
        }
    }

    // 获取所有用户
    suspend fun queryAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            queries.queryAllUsers().executeAsList()
        }
    }

    // 获取特定用户
    suspend fun queryUserById(userId: Long): User? {
        return withContext(Dispatchers.IO) {
            queries.queryUserById(userId).executeAsOneOrNull()
        }
    }

    // 更新用户
    suspend fun updateUser(userId: Long, username: String, email: String?, password_hash: String) {
        withContext(Dispatchers.IO) {
            queries.updateUser(username, email, password_hash, userId)
        }
    }

    // 删除用户
    suspend fun deleteUser(userId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteUser(userId)
        }
    }

    // ---- Envelope 表操作 ----

    // 插入信封
    suspend fun insertEnvelope(userId: Long, name: String, total_budquery: Double, used_budquery: Double, remaining_budquery: Double, created_at: String) {
        withContext(Dispatchers.IO) {
            queries.insertEnvelope(userId, name, total_budquery, used_budquery, remaining_budquery, created_at)
        }
    }

    // 获取某用户的所有信封
    suspend fun queryEnvelopesByUserId(userId: Long): List<Envelope> {
        return withContext(Dispatchers.IO) {
            queries.queryEnvelopesByUserId(userId).executeAsList()
        }
    }

    // 更新信封
    suspend fun updateEnvelopeBudget(envelopeId: Long, total_budquery: Double, used_budquery: Double, remaining_budquery: Double) {
        withContext(Dispatchers.IO) {
            queries.updateEnvelopeBudget(total_budquery, used_budquery, remaining_budquery, envelopeId)
        }
    }

    // 删除信封
    suspend fun deleteEnvelope(envelopeId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteEnvelope(envelopeId)
        }
    }

    // ---- Bill 表操作 ----

    // 插入账单
    suspend fun insertBill(userId: Long, envelopeId: Long, amount: Double, billType: String, billDate: String, paymentMethod: String?, description: String?) {
        withContext(Dispatchers.IO) {
            queries.insertBill(userId, envelopeId, amount, billType, billDate, paymentMethod, description)
        }
    }

    // 获取某信封的所有账单
    suspend fun queryBillsByEnvelopeId(envelopeId: Long): List<Bill> {
        return withContext(Dispatchers.IO) {
            queries.queryBillsByEnvelopeId(envelopeId).executeAsList()
        }
    }

    // 获取某用户的所有账单
    suspend fun queryBillsByUserId(userId: Long): List<Bill> {
        return withContext(Dispatchers.IO) {
            queries.queryBillsByUserId(userId).executeAsList()
        }
    }

    // 更新账单
    suspend fun updateBill(billId: Long, amount: Double, billType: String, billDate: String, paymentMethod: String?, description: String?) {
        withContext(Dispatchers.IO) {
            queries.updateBill(amount, billType, billDate, paymentMethod, description, billId)
        }
    }

    // 删除账单
    suspend fun deleteBill(billId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteBill(billId)
        }
    }

    // ---- Account 表操作 ----

    // 插入账户
    suspend fun insertAccount(userId: Long, accountName: String, balance: Double, createdAt: String) {
        withContext(Dispatchers.IO) {
            queries.insertAccount(userId, accountName, balance, createdAt)
        }
    }

    // 获取某用户的所有账户
    suspend fun queryAccountsByUserId(userId: Long): List<Account> {
        return withContext(Dispatchers.IO) {
            queries.queryAccountsByUserId(userId).executeAsList()
        }
    }

    // 更新账户余额
    suspend fun updateAccount(accountId: Long, balance: Double) {
        withContext(Dispatchers.IO) {
            queries.updateAccountBalance(balance, accountId)
        }
    }

    // 删除账户
            suspend fun deleteAccount(accountId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteAccount(accountId)
        }
    }

    suspend fun queryAllAccount():List<Account>{
        return queries.queryAllAccount().executeAsList()
    }
}
