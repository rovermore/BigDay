package com.smallworldfs.moneytransferapp.data.transactions.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.transactions.model.MyActivityResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class TransactionsNetworkDatasource(private val service: TransactionsService) : NetworkDatasource() {

    fun requestUserTransactions(userId: String, userToken: String, type: String): OperationResult<MyActivityResponse, APIError> =
        executeCall(service.requestUserTransactions(userId, userToken, type))
}
