package com.smallworldfs.moneytransferapp.data.transactions.repository

import com.smallworldfs.moneytransferapp.data.status.mappers.TransactionDTOMapper
import com.smallworldfs.moneytransferapp.data.transactions.model.MyActivityResponse
import com.smallworldfs.moneytransferapp.data.transactions.model.TransactionsInfo
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsHistoryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsSummaryDTO
import javax.inject.Inject

class MyActivityMapper @Inject constructor(
    private val transactionDTOMapper: TransactionDTOMapper,
) {
    fun map(myActivityResponse: MyActivityResponse) = TransactionsHistoryDTO(
        myActivityResponse.msg,
        transactionDTOMapper.map(
            myActivityResponse.transactions,
        ),
        mapTransactionsInfo(myActivityResponse.transactionsInfo),
        myActivityResponse.cancellationMessage,
    )

    private fun mapTransactionsInfo(transactionsInfo: TransactionsInfo) = TransactionsSummaryDTO(
        transactionsInfo.moneyReceived,
        transactionsInfo.moneySent,
        transactionsInfo.transactionsCount
    )
}
