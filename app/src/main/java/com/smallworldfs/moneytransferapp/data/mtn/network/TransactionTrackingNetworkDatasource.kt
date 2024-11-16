package com.smallworldfs.moneytransferapp.data.mtn.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.mtn.model.MTNRequest
import com.smallworldfs.moneytransferapp.data.mtn.model.TransactionTrackingResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class TransactionTrackingNetworkDatasource(
    private val service: TransactionTrackingService
) : NetworkDatasource() {

    fun trackTransaction(request: MTNRequest): OperationResult<TransactionTrackingResponse, APIError> =
        executeCall(service.trackTransaction(request))
}
