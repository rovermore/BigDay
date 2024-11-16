package com.smallworldfs.moneytransferapp.domain.migrated.operations.model

data class IntegrityDTO(
    val nonce: String,
    val requestInfo: RequestInfoDTO
)

data class RequestInfoDTO(
    val signature: String
)
