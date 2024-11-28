package me.accountbook.utils.file

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object EncryptionUtils {

    // 加密方法
    @OptIn(ExperimentalEncodingApi::class)
    fun encrypt(data: String, key: String): String {
        val cipher = Cipher.getInstance("AES")
        val secretKey: Key = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encode(encryptedBytes)
    }

    // 解密方法
    @OptIn(ExperimentalEncodingApi::class)
    fun decrypt(encryptedData: String, key: String): String {
        val cipher = Cipher.getInstance("AES")
        val secretKey: Key = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedData)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    // 生成一个随机的 128 位密钥（16 字节）
    @OptIn(ExperimentalEncodingApi::class)
    fun generateRandomKey(): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128) // 使用 128 位密钥
        val secretKey = keyGenerator.generateKey()
        return Base64.encode(secretKey.encoded)
    }
}
