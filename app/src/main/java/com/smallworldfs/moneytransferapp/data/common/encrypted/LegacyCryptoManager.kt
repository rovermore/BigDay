package com.smallworldfs.moneytransferapp.data.common.encrypted

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Store
import javax.crypto.SecretKey
import javax.inject.Inject

class LegacyCryptoManager @Inject constructor(
    private val crypto: Crypto,
    var store: Store,
    private val storeKeyMapper: StoreKeyMapper
) : CryptoManager {

    override fun decryptData(alias: String, encryptedData: CharArray): CharArray {
        val key = store.getSymmetricKey(alias, null)

        return if (key != null) {
            try {
                crypto.decrypt(String(encryptedData), key)?.toCharArray() ?: STRING_EMPTY.toCharArray()
            } catch (e: IndexOutOfBoundsException) {
                STRING_EMPTY.toCharArray()
            }
        } else {
            val oldKey = store.getSymmetricKey(storeKeyMapper.map(alias), null)
            if (oldKey != null) {
                try {
                    crypto.decrypt(String(encryptedData), oldKey)?.toCharArray() ?: STRING_EMPTY.toCharArray()
                } catch (e: IndexOutOfBoundsException) {
                    STRING_EMPTY.toCharArray()
                }
            } else
                STRING_EMPTY.toCharArray()
        }
    }

    override fun encryptData(alias: String, data: CharArray): String {
        val key: SecretKey = if (!store.hasKey(alias)) {
            store.generateSymmetricKey(alias, null)
        } else {
            store.getSymmetricKey(alias, null)
        }
        val stringData = StringBuilder()
        for (char in data) {
            stringData.append(char)
        }
        return crypto.encrypt(stringData.toString(), key)
    }

    override fun deleteKey(alias: String): Boolean {
        store.deleteKey(alias)
        return true
    }

    override fun keyExists(alias: String): Boolean {
        return store.hasKey(alias)
    }
}
