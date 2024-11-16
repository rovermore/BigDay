package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import javax.inject.Inject

class CancelledTransactionUIModelMapper @Inject constructor() {

    fun map(message: String, transaction: TransactionUIModel): CancelledTransactionUIModel =
        CancelledTransactionUIModel(
            message,
            transaction,
        )
}
