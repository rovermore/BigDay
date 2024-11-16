package com.smallworldfs.moneytransferapp.domain.migrated.status.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CancelledTransactionDTO(
    val text: String = STRING_EMPTY,
    val msg: String = STRING_EMPTY
)
