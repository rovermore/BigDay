package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.StatusTransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO

object StatusTransactionDTOMock {

    private val transaction1 = TransactionDTO()
    private val transaction2 = TransactionDTO()
    private val transaction3 = TransactionDTO()

    val transactions = listOf(transaction1, transaction2, transaction3)

    val statusTransactionDTO = StatusTransactionDTO(
        false,
        transactions,
        3,
        "cancellation message"
    )
}
