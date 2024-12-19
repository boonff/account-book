package me.accountbook.network.manager

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import me.accountbook.data.model.SerDataItem
import me.accountbook.utils.LoggingUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@OptIn(ExperimentalSerializationApi::class)
object SyncUtil : KoinComponent {
    const val PROTOBUF_SUFFIX = ".proto"
    const val MAIN_APP_PATH = "appData"
    private const val TIMESTAMP_PATH = "timestamp"
    val repositoryManager: RepositoryManager by inject()

    suspend inline fun <reified T : List<SerDataItem>> uploadTable(
        tableName: String,
        table: T
    ): Boolean {
        val protoBufBytes = ProtoBuf.encodeToByteArray(table)
        return repositoryManager.upload("$MAIN_APP_PATH/$tableName$PROTOBUF_SUFFIX ", protoBufBytes)
    }

    suspend inline fun <reified T : SerDataItem> fetchTable(tableKey: String): List<T>? {
        val protoBufBytes =
            repositoryManager.fetch("$MAIN_APP_PATH/$tableKey$PROTOBUF_SUFFIX ") ?: run {
                LoggingUtil.logDebug("表下载或解码失败")
                return null
            }
        return ProtoBuf.decodeFromByteArray<List<T>>(protoBufBytes)
    }


    suspend fun uploadTimestamp(timestamp: Long, tableKey: String): Boolean {
        val protoBufBytes = ProtoBuf.encodeToByteArray(timestamp.toString())
        return repositoryManager.upload("$TIMESTAMP_PATH/$tableKey$PROTOBUF_SUFFIX", protoBufBytes)
    }

    suspend fun fetchTimestamp(tableKey: String): Long? {
        val protoBufBytes =
            repositoryManager.fetch("$TIMESTAMP_PATH/$tableKey$PROTOBUF_SUFFIX") ?: run {
                LoggingUtil.logDebug("tableTimestamp 下载或解码失败")
                return null
            }
        return ProtoBuf.decodeFromByteArray<String>(protoBufBytes).toLong()
    }

}
