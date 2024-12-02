package me.accountbook.network

import me.accountbook.WebViewManager
import me.accountbook.network.utils.ServerUtil
import me.accountbook.utils.file.FileUtil
import me.accountbook.utils.file.KeystoreUtil
import org.koin.core.component.inject

object AndroidUserService : UserService() {

    private const val KEY_ALIAS = "access_token"
    private val keystoreUtil = KeystoreUtil(KEY_ALIAS)
    private val webViewManager: WebViewManager by inject()

    private val fileUtil: FileUtil by inject()


    override suspend fun saveTokenFile(token:String): Boolean {
        val encryptToken = keystoreUtil.encryptData(token)
        return if (fileUtil.save(tokenPath, encryptToken)) {
            webViewManager.closeWebViewActivity()
            true
        } else
            false
    }

    override suspend fun deleteTokenFile(): Boolean {
        return fileUtil.delete(tokenPath)
    }

    override fun readTokenFile(): String? {
        val encryptToken = fileUtil.read(tokenPath)
        return if (encryptToken == null) null
        else
            keystoreUtil.decryptData(encryptToken)
    }


}