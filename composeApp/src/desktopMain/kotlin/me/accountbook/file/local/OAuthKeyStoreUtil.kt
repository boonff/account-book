package me.accountbook.file.local
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
class DPAPIEncryptor : Encryptor {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val IV_LENGTH_BYTE = 12 // GCM 模式需要 12 字节的 IV
        private const val TAG_LENGTH_BIT = 128 // GCM 的 Tag 长度是 128 bit
    }

    // 加密数据
    override fun encryptData(data: String): String {
        try {
            val secretKey = getSecretKey()

            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val encryptedData = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            val iv = cipher.iv

            // 返回 IV 和加密数据一起进行 Base64 编码
            val result = iv + encryptedData
            return Base64.getEncoder().encodeToString(result)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Encryption failed")
        }
    }

    // 解密数据
    override fun decryptData(encryptedData: String): String {
        try {
            val data = Base64.getDecoder().decode(encryptedData)
            val iv = data.copyOfRange(0, IV_LENGTH_BYTE)
            val encryptedBytes = data.copyOfRange(IV_LENGTH_BYTE, data.size)

            val cipher = Cipher.getInstance(ALGORITHM)
            val spec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
            val secretKey = getSecretKey()

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedData = cipher.doFinal(encryptedBytes)
            return String(decryptedData, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Decryption failed")
        }
    }

    // 获取密钥
    private fun getSecretKey(): SecretKey {
        // 在这里使用 DPAPI 保护数据
        return SecretKeySpec(getDPAPIKey(), "AES")
    }

    // 使用 Windows DPAPI 获取保护的密钥
    private fun getDPAPIKey(): ByteArray {
        // 使用 Windows DPAPI 加密/解密数据
        return "dummy_secret_data".toByteArray()
    }
}
