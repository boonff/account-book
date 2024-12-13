package me.accountbook.data.model

import kotlinx.serialization.Serializable
import me.accountbook.database.Account
import me.accountbook.database.Tagbox
import java.time.Instant

interface SerDataItem {
    val uuid: String
    var deleted: Long?
    var timestamp: Long?
}


@Serializable
data class NetModel<T>(
    val serTable: List<T>? = null,
    var timestamp: Long?
)


//Tagbox 表
@Serializable
data class SerTagbox(
    override val uuid: String,
    var name: String,
    var color: Long,
    var position: Long? = null,
    override var timestamp: Long? = null,
    override var deleted: Long? = null,
) : SerDataItem

fun SerTagbox.decode() =
    Tagbox(uuid, name, color, position, timestamp, deleted)

fun Tagbox.encode() =
    SerTagbox(uuid, name, color, position, timestamp, deleted)


//Account 表

@Serializable
data class SerAccount(
    override val uuid: String,
    override var timestamp: Long? = null,
    override var deleted: Long? = null,
    var name: String,
    var balance: Double,
    var targetSavings: Double,
    var emergencySavings: Double,
    var position: Long? = null,
) : SerDataItem

fun SerAccount.decode() =
    Account(uuid, timestamp, deleted, name, balance, targetSavings, emergencySavings, position)

fun Account.encode() =
    SerAccount(
        uuid,
        timestamp,
        deleted,
        name,
        balance,
        targetSavings,
        emergencySavings,
        position
    )

