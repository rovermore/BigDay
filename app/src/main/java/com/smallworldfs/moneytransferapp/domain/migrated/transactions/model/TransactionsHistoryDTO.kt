package com.smallworldfs.moneytransferapp.domain.migrated.transactions.model

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO

data class TransactionsHistoryDTO(
    val msg: String,
    val transactions: List<TransactionDTO>,
    val transactionsSummary: TransactionsSummaryDTO,
    val cancellationMessage: String
)

data class TransactionsSummaryDTO(
    val moneyReceived: Double,
    val moneySent: Double,
    val transactionsCount: Int
)
