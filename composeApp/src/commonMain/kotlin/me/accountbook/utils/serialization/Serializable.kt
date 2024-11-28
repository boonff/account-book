package me.accountbook.utils.serialization

import kotlinx.serialization.Serializable
import me.accountbook.database.Tagbox

@Serializable
data class SerializableTagbox(
    override val uuid: String,
    val name: String,
    val color: Long,
    val position: Long,
    override val timestamp: Long?,
    val deleted: Long?,
) : DBItem

@Serializable
data class SerializableDatabase(
    val serializableTagboxs: List<SerializableTagbox>,
    val timestamp: Long? = null,
)

fun Tagbox.encode() =
    SerializableTagbox(uuid, name, color, position, timestamp, deleted)

fun SerializableTagbox.decode() =
    Tagbox(uuid, name, color, position, timestamp, deleted)

interface DBItem {
    val uuid: String
    val timestamp: Long?
}