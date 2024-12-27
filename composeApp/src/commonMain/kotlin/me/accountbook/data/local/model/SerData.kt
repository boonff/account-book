package me.accountbook.data.local.model

import kotlinx.serialization.Serializable
import me.accountbook.database.Account
import me.accountbook.database.TableTimestamp
import me.accountbook.database.Tagbox

interface SerDataItem {
    val uuid: String
    var deleted: Long?
    var timestamp: Long?
    fun decode(): Any
}

@Serializable
data class SerTableTimestamp(
    val name: String,
    val netTimestamp: Long?,
    val localTimestamp: Long?,
) {
    fun decode() = TableTimestamp(name, netTimestamp, localTimestamp)
}

fun TableTimestamp.encode() = SerTableTimestamp(name, netTimestamp, localTimestamp)

//Tagbox 表
@Serializable
data class SerTagbox(
    override val uuid: String,
    var name: String,
    var color: Long,
    var position: Long? = null,
    override var timestamp: Long? = null,
    override var deleted: Long? = null,
) : SerDataItem {
    override fun decode() = Tagbox(uuid, name, color, position, timestamp, deleted)
}

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
) : SerDataItem {
    override fun decode() =
        Account(uuid, timestamp, deleted, name, balance, targetSavings, emergencySavings, position)
}

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

