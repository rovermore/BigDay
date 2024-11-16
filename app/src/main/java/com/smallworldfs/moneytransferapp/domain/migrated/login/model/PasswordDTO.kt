package com.smallworldfs.moneytransferapp.domain.migrated.login.model

class PasswordDTO(
    val code: CharArray
) {
    fun release() {
        for (k in code.indices) {
            code[k] = ' '
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordDTO

        if (!code.contentEquals(other.code)) return false

        return true
    }

    override fun hashCode(): Int {
        return code.contentHashCode()
    }
}
