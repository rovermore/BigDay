package com.smallworldfs.moneytransferapp.presentation.autentix.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class AutentixMessage(
    val eventType: String,
    @SerializedName("payload")
    val payload: Payload,
) {
    data class Payload(
        @SerializedName("value")
        val value: String = STRING_EMPTY,
        @SerializedName("message")
        val message: String = STRING_EMPTY
    )
}
