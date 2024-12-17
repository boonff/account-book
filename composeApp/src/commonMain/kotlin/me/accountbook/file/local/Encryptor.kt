package me.accountbook.file.local

interface Encryptor {
    fun encryptData(data: String): String
    fun decryptData(encryptedData: String): String
}