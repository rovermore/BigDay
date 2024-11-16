package com.smallworldfs.moneytransferapp.data.base.network.models

data class ErrorResponse(
    val error: Error = Error()
) {
    data class Error(
        val exceptionMessage: String = "",
        val message: String = "",
        val subCode: String = ""
    )
}
