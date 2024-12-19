package me.accountbook.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.accountbook.data.local.helper.TagboxHelper
import me.accountbook.database.Tagbox
import java.util.UUID

class TagboxRepository(dbHelper: TagboxHelper) : AppDatabaseRepository<Tagbox>(dbHelper) {

    // 插入一个 Tagbox
    suspend fun insert(name: String, color: Color): Boolean {
        return withContext(Dispatchers.IO) {
            super.insert(
                Tagbox(
                    uuid = UUID.randomUUID().toString(),
                    name = name,
                    color = color.toArgb().toUInt().toLong(),
                    position = null,
                    timestamp = null,
                    deleted = null,
                )
            )
        }
    }

    // 更新 Tagbox 名称
    suspend fun updateName(name: String, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
            updateProperty(uuid) {
                it.copy(name = name)
            }
        }
    }

    // 更新 Tagbox 位置
    suspend fun updatePosition(position: Int, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
            updateProperty(uuid) {
                it.copy(position = position.toLong())
            }
        }
    }

    // 更新 Tagbox 颜色
    suspend fun updateColor(color: Color, uuid: String): Boolean {
        return withContext(Dispatchers.IO) {
            updateProperty(uuid) {
                it.copy(color = color.toArgb().toUInt().toLong())
            }
        }
    }

}