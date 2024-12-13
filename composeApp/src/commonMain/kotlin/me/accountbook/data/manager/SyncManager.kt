package me.accountbook.data.manager

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import me.accountbook.data.model.NetModel
import me.accountbook.data.model.SerAccount
import me.accountbook.data.model.SerTagbox
import me.accountbook.data.repository.AccountRepository
import me.accountbook.data.repository.TagboxRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object TagboxSyncManager : SyncHandler<SerTagbox>(), KoinComponent {
    private val tagboxRepository: TagboxRepository by inject()
    private const val REPO_FILE_NAME = "tagbox.db"

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun sync(): Boolean {
        val netModel =
            ProtoBuf.decodeFromByteArray<NetModel<SerTagbox>>(fetchFile(REPO_FILE_NAME))
        val localTable = tagboxRepository.query() ?: emptyList()
        val netTable = netModel.serTable ?: emptyList()

        val merged = mergeItems(netTable, localTable, netModel.timestamp)
        tagboxRepository.refactor(merged)
        return userService.uploadToRepo(
            REPO_FILE_NAME,
            createNetTableModel(merged)
        ).also { isSynced = it }
    }
}

object AccountSyncManager : SyncHandler<SerAccount>(), KoinComponent {
    private val accountRepository: AccountRepository by inject()
    private const val REPO_FILE_NAME = "account.db"

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun sync(): Boolean {
        val netModel =
            ProtoBuf.decodeFromByteArray<NetModel<SerAccount>>(
                fetchFile(
                    REPO_FILE_NAME
                )
            )
        val localTable = accountRepository.query() ?: emptyList()
        val netTable = netModel.serTable ?: emptyList()
        val merged = mergeItems(netTable, localTable, netModel.timestamp)
        accountRepository.refactor(merged)
        return userService.uploadToRepo(
            REPO_FILE_NAME,
            createNetTableModel(merged)
        ).also { isSynced = it }
    }
}