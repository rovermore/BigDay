package com.smallworldfs.moneytransferapp.data.status.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDetailsDTO
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionDetailResponse
import javax.inject.Inject

class TransactionDetailsDTOMapper @Inject constructor(
    private val transactionDTOMapper: TransactionDTOMapper
) {

    fun map(transactionDetails: TransactionDetailResponse) = with(transactionDetails) {
        TransactionDetailsDTO(
            msg,
            transactionDTOMapper.map(transaction),
            cancellationMessage
        )
    }
}
