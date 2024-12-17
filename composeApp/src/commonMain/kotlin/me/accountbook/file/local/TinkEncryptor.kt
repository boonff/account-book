//package me.accountbook.file.local
//
//import com.google.crypto.tink.Aead
//import com.google.crypto.tink.KeysetHandle
//import com.google.crypto.tink.config.TinkConfig
//import com.google.crypto.tink.subtle.Base64
//import com.google.crypto.tink.aead.AesGcmKeyManager
//import com.google.crypto.tink.aead.AesGcmKey
//import javax.crypto.SecretKey
//
//class TinkEncryptor(private val secretKey: SecretKey) : Encryptor {
//
//    companion object {
//        private const val ALGORITHM = "AES/GCM/NoPadding"
//        private const val IV_LENGTH_BYTE = 12 // GCM 模式需要 12 字节的 IV
//        private const val TAG_LENGTH_BIT = 128 // GCM 的 Tag 长度是 128 bit
//    }
//
//    init {
//        TinkConfig.register() // 初始化 Tink 配置
//    }
//
//    // 使用 Tink 加密数据
//    override fun encryptData(data: String): String {
//        val aead: Aead = createAead(secretKey)
//
//        val encryptedData = aead.encrypt(data.toByteArray(), null)
//
//        // 获取生成的 IV
//        val iv = encryptedData.copyOfRange(0, IV_LENGTH_BYTE)
//        val encryptedBytes = encryptedData.copyOfRange(IV_LENGTH_BYTE, encryptedData.size)
//
//        // 将 IV 和加密数据一起返回
//        val result = iv + encryptedBytes
//        return Base64.encode(result) // 使用 Tink 的 Base64 编码
//    }
//
//    // 使用 Tink 解密数据
//    override fun decryptData(encryptedData: String): String {
//        val aead: Aead = createAead(secretKey)
//
//        val data = Base64.decode(encryptedData) // 使用 Tink 的 Base64 解码
//
//        // 提取 IV 和密文
//        val iv = data.copyOfRange(0, IV_LENGTH_BYTE)
//        val encryptedBytes = data.copyOfRange(IV_LENGTH_BYTE, data.size)
//
//        val decryptedData = aead.decrypt(encryptedBytes, null)
//        return String(decryptedData)
//    }
//
//    // 使用 SecretKey 创建 Aead 实例
//    private fun createAead(secretKey: SecretKey): Aead {
//        val keyData = AesGcmKey.newBuilder()
//            .setKeyValue(secretKey.encoded)
//            .build()
//        val keysetHandle = KeysetHandle.newBuilder()
//            .add(keyData)
//            .build()
//
//        return keysetHandle.getPrimitive(Aead::class.java)
//    }
//}
