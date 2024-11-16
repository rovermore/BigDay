package com.smallworldfs.moneytransferapp.data.account.documents.model

data class TypesOfDocumentsResponse(
    val active: Int?,
    val backend: Int?,
    val country: String?,
    val document: String?,
    val frontend: Int?,
    val order: Int?,
    val trans: String?,
    val validate_backend: Int?,
    val validate_frontend: Int?
)
