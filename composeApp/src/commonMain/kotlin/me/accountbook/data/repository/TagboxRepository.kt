package me.accountbook.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import me.accountbook.data.local.DatabaseHelper
import me.accountbook.data.local.TagboxHelper
import me.accountbook.data.model.SerTagbox
import java.util.UUID

class TagboxRepository(dbHelper: TagboxHelper) : DatabaseRepository<SerTagbox>(dbHelper) {

    fun insert(name: String, color: Color) {
        super.insert(
            SerTagbox(
                uuid = UUID.randomUUID().toString(),
                name = name,
                color = color.toArgb().toUInt().toLong(),
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