package com.smallworldfs.moneytransferapp.data.transactions.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.transactions.network.TransactionsNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.flatMapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.model.TransactionsHistoryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.repository.TransactionsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsNetworkDatasource: TransactionsNetworkDatasource,
    private val myActivityMapper: MyActivityMapper,
    private val userDataRepository: UserDataRepository,
    private val apiErrorMapper: APIErrorMapper
) : TransactionsRepository {

    override fun getUserTransactions(): OperationResult<TransactionsHistoryDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return transactionsNetworkDatasource.requestUserTransactions(
                    it.id,
                    it.userToken,
                    "sender",
                )
                    .mapFailure {
                        apiErrorMapper.map(it)
                    }.map { myActivityResponse ->
                        myActivityMapper.map(myActivityResponse)
                    }
            }.flatMapFailure { Failure<Error>(Error.UncompletedOperation("Could not retrieve user settings")) }
    }
}
