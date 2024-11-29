package me.accountbook.utils.serialization

import kotlinx.serialization.Serializable
import me.accountbook.database.Account
import me.accountbook.database.Tagbox

interface DBItem {
    val uuid: String
    val timestamp: Long?
}


@Serializable
data class SerializableDatabase(
    val serializableTagboxs: List<SerializableTagbox>,
    val serializableAccount: List<SerializableAccount>,
    var timestamp: Long? = null,//默认参数必须在最后
)


//Tagbox 表
@Serializable
data class SerializableTagbox(
    override val uuid: String,
    val name: String,
    val color: Long,
    val position: Long,
    override val timestamp: Long?,
    val deleted: Long?,
) : DBItem

fun SerializableTagbox.decode() =
    Tagbox(uuid, name, color, position, timestamp, deleted)

fun Tagbox.encode() =
    SerializableTagbox(uuid, name, color, position, timestamp, deleted)


//Account 表

@Serializable
data class SerializableAccount(
    override val uuid: String,
    override val timestamp: Long?,
    val deleted: Long?,
    val name: String,
    val balance: Double,
    val minBalance: Double?,
) : DBItem

fun SerializableAccount.encode() =
    SerializableAccount(uuid, timestamp, deleted, name, balance, minBalance)

fun Account.encode() =
    SerializableAccount(uuid, timestamp, deleted, name, balance, minBalance)

