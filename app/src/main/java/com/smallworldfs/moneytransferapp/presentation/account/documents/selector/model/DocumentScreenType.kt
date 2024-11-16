package com.smallworldfs.moneytransferapp.presentation.account.documents.selector.model

import java.util.Locale

enum class DocumentScreenType {
    SEND,
    ATTACH,
    EDIT,
    ITALY,
    NONE;

    override fun toString(): String {
        return super.toString().lowercase(Locale.getDefault())
    }

    companion object {
        @JvmStatic
        fun toEnum(mode: String?): DocumentScreenType? {
            mode?.let {
                return when (mode.lowercase(Locale.getDefault())) {
                    SEND.toString() -> SEND
                    ATTACH.toString() -> ATTACH
                    EDIT.toString() -> EDIT
                    ITALY.toString() -> ITALY
                    NONE.toString() -> NONE
                    else -> null
                }
            }
            return null
        }
    }
}
