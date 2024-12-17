package me.accountbook

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.accountbook.data.local.InitDatabaseUtil
import me.accountbook.data.manager.domain.TagboxManager
import me.accountbook.network.manager.UserManager
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppInitializer : KoinComponent {
    private val initDB: InitDatabaseUtil by inject()
    private val tagboxManager: TagboxManager by inject()
    private val userManager: UserManager by inject()
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initialize() {
        appScope.launch {
            initDB.initDatabase()
            userManager.initUser {
                tagboxManager.refreshTimestamp()
            }
        }
    }
}