package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsHistoryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsSummaryDTO

object TransactionsHistoryDTOMock {

    private val transactionsSummaryDTO = TransactionsSummaryDTO(
        34.34,
        54.23,
        3
    )

    private val transaction1 = TransactionDTO()
    private val transaction2 = TransactionDTO()
    private val transaction3 = TransactionDTO()

    private val transactions = listOf(transaction1, transaction2, transaction3)

    val transactionsHistoryDTO = TransactionsHistoryDTO(
        "msn",
        transactions,
        transactionsSummaryDTO,
        "cancellation message"
    )
}
