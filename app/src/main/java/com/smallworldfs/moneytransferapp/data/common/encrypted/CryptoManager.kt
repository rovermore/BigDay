package com.smallworldfs.moneytransferapp.data.common.encrypted

interface CryptoManager {
    fun decryptData(alias: String, encryptedData: CharArray): CharArray
    fun encryptData(alias: String, data: CharArray): String
    fun deleteKey(alias: String): Boolean
    fun keyExists(alias: String): Boolean
}
