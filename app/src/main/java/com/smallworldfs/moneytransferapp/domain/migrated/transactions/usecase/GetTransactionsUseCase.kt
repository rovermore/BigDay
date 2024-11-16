package com.smallworldfs.moneytransferapp.domain.migrated.transactions.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsHistoryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.repository.TransactionsRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val transactionsRepository: TransactionsRepository) {

    fun getUserTransactions(): OperationResult<TransactionsHistoryDTO, Error> =
        transactionsRepository.getUserTransactions()
}
