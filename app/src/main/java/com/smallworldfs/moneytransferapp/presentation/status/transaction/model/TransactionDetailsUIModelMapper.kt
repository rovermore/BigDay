package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDetailsDTO
import javax.inject.Inject

class TransactionDetailsUIModelMapper @Inject constructor(
    private val transactionUIModelMapper: TransactionUIModelMapper
) {

    fun map(transactionDetailsDTO: TransactionDetailsDTO) = with(transactionDetailsDTO) {
        TransactionDetailsUIModel(
            transactionUIModelMapper.map(cancellationMessage, transaction),
            cancellationMessage,
        )
    }
}
