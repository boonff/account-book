package me.accountbook.file.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeystoreUtil(private val keyAlias: String) : Encryptor {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val IV_LENGTH_BYTE = 12 // GCM 模式需要 12 字节的 IV
        private const val TAG_LENGTH_BIT = 128 // GCM 的 Tag 长度是 128 bit
    }

    init {
        if (!isKeyAvailable())
            generateAndStoreKey()
    }

    // 生成并存储密钥
    private fun generateAndStoreKey() {
        try {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenSpec)
            keyGenerator.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 获取密钥
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    // 使用 Keystore 加密数据
    override fun encryptData(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val secretKey = getSecretKey()

        // 不需要手动生成 IV，Keystore 会自动生成
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedData = cipher.doFinal(data.toByteArray())

        // 获取生成的 IV
        val iv = cipher.iv

        // 将 IV 和加密数据一起返回
        val result = iv + encryptedData
        return Base64.encodeToString(result, Base64.DEFAULT)
    }


    // 使用 Keystore 解密数据
    override fun decryptData(encryptedData: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val data = Base64.decode(encryptedData, Base64.DEFAULT)

        // 提取 IV 和密文
        val iv = data.copyOfRange(0, IV_LENGTH_BYTE)
        val encryptedBytes = data.copyOfRange(IV_LENGTH_BYTE, data.size)

        // 使用提取的 IV 初始化解密
        val spec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        val decryptedData = cipher.doFinal(encryptedBytes)
        return String(decryptedData)
    }


    // 检查 Keystore 中是否已存在密钥
    private fun isKeyAvailable(): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.containsAlias(keyAlias)
    }

}
