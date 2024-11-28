package me.accountbook.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.utils.serialization.SerializableTagbox
import java.sql.SQLException
import java.util.UUID

class DatabaseHelper(private val driver: SqlDriver) {
    private val triggerHelper = TriggerHelper(driver)
    private val database = Database(driver) // 自动生成的数据库类
    private val queries = database.appDatabaseQueries

    fun initializeDatabase() {

        queries.dropTagboxTimstampTragger()

        queries.createSetPositionTrigger()
        queries.createTagboxTimstampTragger()
    }

    // tagbox表的方法
    suspend fun insertTagBox(name: String, color: Color) {
        try {
            withContext(Dispatchers.IO) {
                queries.insertTagbox(
                    UUID.randomUUID().toString(),
                    name,
                    color.toArgb().toUInt().toLong(),
                )
            }
        } catch (e: SQLException) {
            println("Error inserting tag box: ${e.message}")
            throw e
        }
    }

    suspend fun insertTagBox(tagbox: Tagbox) {
        try {
            withContext(Dispatchers.IO) {
                queries.insertTagboxWithTimetamp(
                    tagbox.uuid,
                    tagbox.name,
                    tagbox.color,
                    tagbox.position,
                    tagbox.timestamp,
                    tagbox.deleted,
                )
            }
        } catch (e: SQLException) {
            println("Error inserting tagbox: ${e.message}")
        }
    }

    suspend fun insertTagBox(tagbox: SerializableTagbox) {
        try {
            withContext(Dispatchers.IO) {
                queries.insertTagboxWithTimetamp(
                    tagbox.uuid,
                    tagbox.name,
                    tagbox.color,
                    tagbox.position,
                    tagbox.timestamp,
                    tagbox.deleted,
                )
            }
        } catch (e: SQLException) {
            println("Error inserting tagbox: ${e.message}")
        }
    }

    suspend fun queryUndeletedTagBox(): List<Tagbox> {
        return try {
            withContext(Dispatchers.IO) {
                queries.queryUndeletedTagbox().executeAsList()
            }
        } catch (e: SQLException) {
            println("Error querying undeleted tag boxes: ${e.message}")
            emptyList()
        }
    }

    suspend fun queryAllTagbox(): List<Tagbox> {
        return try {
            withContext(Dispatchers.IO) {
                queries.queryAllTagbox().executeAsList()
            }
        } catch (e: SQLException) {
            println("Error querying all tagbox: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateTagboxName(name: String, uuid: String) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxName(name, uuid)
            }
        } catch (e: SQLException) {
            println("Error updateTagboxName:${e.message}")
        }
    }

    suspend fun updateTagboxColor(color: Color, uuid: String) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxColor(color.toArgb().toUInt().toLong(), uuid)
            }
        } catch (e: SQLException) {
            println("Error updateTagboxColor${e.message}")
        }
    }

    suspend fun updateTagboxPosition(position: Int, uuid: String) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxPosition(position.toLong(), uuid)
            }
        } catch (e: SQLException) {
            println("Error updateTagboxPosition: ${e.message}")
        }
    }

    suspend fun updateTagbox(tagbox: Tagbox) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxById(
                    tagbox.name,
                    tagbox.color,
                    tagbox.position,
                    tagbox.deleted,
                    tagbox.uuid
                )
            }
        } catch (e: SQLException) {
            println("Error updateTagbox: ${e.message}")
        }
    }

    suspend fun updateTagbox(tagbox: SerializableTagbox) {
        try {
            withContext(Dispatchers.IO) {
                queries.updateTagboxByUuid(
                    tagbox.name,
                    tagbox.color,
                    tagbox.position,
                    tagbox.deleted,
                    tagbox.uuid
                )
            }
        } catch (e: SQLException) {
            println("Error updateTagbox: ${e.message}")
        }
    }

    suspend fun softDeleteTagbox(uuid: String) {
        try {
            withContext(Dispatchers.IO) {
                queries.softDeleteTagbox(uuid)
            }
        } catch (e: SQLException) {
            println("Error softDeleteTagbox")
        }
    }

    // 账户表的方法
    suspend fun queryAllAccount(): List<Account> {
        return try {
            withContext(Dispatchers.IO) {
                queries.queryAllAccount().executeAsList()
            }
        } catch (e: SQLException) {
            println("Error querying all accounts: ${e.message}")
            emptyList() // 返回空列表，避免崩溃
        }
    }
}
