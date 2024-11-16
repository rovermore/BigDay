package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CancelledTransactionUIModel(
    val msg: String = STRING_EMPTY,
    val transaction: TransactionUIModel = TransactionUIModel(),
)
