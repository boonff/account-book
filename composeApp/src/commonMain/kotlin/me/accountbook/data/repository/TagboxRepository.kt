package me.accountbook.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import me.accountbook.data.local.helper.TagboxHelper
import me.accountbook.database.Tagbox
import java.util.UUID

class TagboxRepository(dbHelper: TagboxHelper) : AppDatabaseRepository<Tagbox>(dbHelper) {

    fun insert(name: String, color: Color): Boolean {
        return super.insert(
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

    fun updateName(name: String, uuid: String): Boolean {
        return updateProperty(uuid) {
            it.copy(name = name)
        }
    }

    fun updatePosition(position: Int, uuid: String): Boolean {
        return updateProperty(uuid) {
            it.copy(position = position.toLong())
        }
    }

    fun updateColor(color: Color, uuid: String): Boolean {
        return updateProperty(uuid) {
            it.copy(color = color.toArgb().toUInt().toLong())
        }
    }

}