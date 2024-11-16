package com.smallworldfs.moneytransferapp.domain.migrated.status.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class TransactionDetailsDTO(
    val msg: String = STRING_EMPTY,
    val transaction: TransactionDTO = TransactionDTO(),
    val cancellationMessage: String = STRING_EMPTY
)
