package me.accountbook.data.manager.domain

import androidx.compose.ui.graphics.Color
import me.accountbook.data.manager.sync.SyncUtil
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.database.Tagbox
import me.accountbook.utils.LoggingUtil
import me.accountbook.utils.TimestampUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagboxManager : KoinComponent {
    private val tableTimestampRep: TableTimestampRepository by inject()
    private val tagboxRep: TagboxRepository by inject()

    suspend fun refreshTimestamp(): Boolean {
        val netTimestamp = SyncUtil.fetchTimestamp(tagboxRep.tableKey) ?: return false
        return tableTimestampRep.updateNetTimestamp(netTimestamp, tagboxRep.tableKey)
    }

    fun isSynced(): Boolean {
        return tableTimestampRep.isSynced(tagboxRep.tableKey)
    }

    fun unSynced(): Boolean {
        return !isSynced()
    }

    fun queryUnDeleted(): List<Tagbox>? {
        return tagboxRep.queryUndeleted()
    }

    fun insert(name: String, color: Color): Boolean {
        return if (tagboxRep.insert(name, color)) {
            tableTimestampRep.updateLocalTimestamp(TimestampUtil.getTimestamp(), tagboxRep.tableKey)
            true
        } else
            false
    }

    fun updateName(name: String, uuid: String): Boolean {
        return if (tagboxRep.updateName(name, uuid)) {
            tableTimestampRep.updateLocalTimestamp(TimestampUtil.getTimestamp(), tagboxRep.tableKey)
        } else
            false
    }

    fun updateColor(color: Color, uuid: String): Boolean {
        return if (tagboxRep.updateColor(color, uuid)) {
            tableTimestampRep.updateLocalTimestamp(TimestampUtil.getTimestamp(), tagboxRep.tableKey)
        } else
            false

    }

    fun updatePosition(position: Int, uuid: String): Boolean {
        return if (tagboxRep.updatePosition(position, uuid)) {
            tableTimestampRep.updateLocalTimestamp(TimestampUtil.getTimestamp(), tagboxRep.tableKey)
        } else
            false
    }

    fun softDelete(uuid: String): Boolean {
        return if (tagboxRep.softDelete(uuid)) {
            tableTimestampRep.updateLocalTimestamp(TimestampUtil.getTimestamp(), tagboxRep.tableKey)
        } else
            false
    }
}