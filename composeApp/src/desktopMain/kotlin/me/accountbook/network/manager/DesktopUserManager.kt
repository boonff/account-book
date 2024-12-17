package me.accountbook.network.manager

import me.accountbook.file.local.FileUtil
import org.koin.core.component.inject

object DesktopUserManager : UserManager() {

    private val fileUtil: FileUtil by inject()
    override suspend fun saveTokenFile(token: String): Boolean {
        return fileUtil.save(tokenPath, token)
    }

    override suspend fun deleteTokenFile(): Boolean {
        return fileUtil.delete(tokenPath)
    }

    override fun readTokenFile(): String? {
        return fileUtil.read(tokenPath)
    }
}