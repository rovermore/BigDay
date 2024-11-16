package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class TypesOfDocumentsDTO(
    val id: String,
    val name: String,
    val country: String = STRING_EMPTY,
    val validations: Validations,
)

data class Validations(val front: Boolean, val back: Boolean)
