package com.smallworldfs.moneytransferapp.domain.migrated.status.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class StatusTransactionDTO(
    val cancellable: Boolean = false,
    val transactions: List<TransactionDTO> = emptyList(),
    val total: Int = 0,
    val cancellationMessage: String = STRING_EMPTY
)
