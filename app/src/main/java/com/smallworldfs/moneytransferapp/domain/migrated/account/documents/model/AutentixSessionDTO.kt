package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class AutentixSessionDTO(
    val url: String = STRING_EMPTY,
    val externalId: String = STRING_EMPTY,
    val timeout: Long = 0L
)
