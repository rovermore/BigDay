package com.smallworldfs.moneytransferapp.data.operations.model

class OtpDTO(
    val uuid: String,
    val status: String,
    val factorType: String,
    val operationId: String,
    val retries: Int
)
