package com.smallworldfs.moneytransferapp.domain.migrated.login.model

data class PassCodeDTO(
    val code: CharArray
) {
    fun release() {
        for (k in code.indices) {
            code[k] = ' '
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PassCodeDTO || other.code.size != this.code.size) return false
        for (k in code.indices) {
            if (other.code[k] != this.code[k]) return false
        }
        return true
    }

    override fun hashCode(): Int {
        return code.contentHashCode()
    }
}
