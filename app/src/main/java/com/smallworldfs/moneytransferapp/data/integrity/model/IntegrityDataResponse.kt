package com.smallworldfs.moneytransferapp.data.integrity.model

data class IntegrityDataResponse(
    val data: IntegrityNonceResponse,
    val signature: String
)
