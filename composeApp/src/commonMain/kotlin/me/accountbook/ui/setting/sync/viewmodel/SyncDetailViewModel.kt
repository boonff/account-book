package me.accountbook.ui.setting.sync.viewmodel


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import me.accountbook.database.DatabaseHelper
import me.accountbook.network.LoginManager
import me.accountbook.platform.FileStorage
import me.accountbook.utils.serialization.SerializableTagbox
import me.accountbook.utils.serialization.toSerializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncDetailViewModel(private val dbHelper: DatabaseHelper) : ViewModel(), KoinComponent {
    private val fileStorage: FileStorage by inject()
    private val loginManager: LoginManager by inject()
    suspend fun serialization() {
        val tagbox = dbHelper.queryAllTagBox()
        val serializableTagbox =
            tagbox.map {
                it.toSerializable()
            }
        val listSerializer = ListSerializer(SerializableTagbox.serializer())

        val json = Json.encodeToString(listSerializer, serializableTagbox)
        fileStorage.saveJsonToFile("database_tagbox.json", json)
    }

    suspend fun login(){
        loginManager.openLoginPage()
        loginManager.getAccessToken()
    }


}