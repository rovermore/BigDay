package com.smallworldfs.moneytransferapp.domain.migrated.transactions.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsHistoryDTO

interface TransactionsRepository {
    fun getUserTransactions(): OperationResult<TransactionsHistoryDTO, Error>
}
