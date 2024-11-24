package me.accountbook.utils.serialization

import kotlinx.serialization.Serializable
import me.accountbook.database.Tagbox


@Serializable
data class SerializableTagbox(
    val tagboxId: Long,
    val name: String,
    val color: Long,
    val position: Long,
)

fun Tagbox.toSerializable() = SerializableTagbox(tagbox_id, name, color, position)
fun SerializableTagbox.toDatabase() = Tagbox(tagboxId, name, color, position)