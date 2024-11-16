package com.smallworldfs.moneytransferapp.data.calculator.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.calculator.model.CalculateRateResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.DeliveryMethodsResponse

class CalculatorNetworkDatasource(
    private val service: CalculatorService
) : NetworkDatasource() {

    fun getDeliveryMethods(): OperationResult<DeliveryMethodsResponse, APIError> =
        executeCall(service.getDeliveryMethods())

    fun calculateRate(request: Map<String, String>): OperationResult<CalculateRateResponse, APIError> =
        executeCall(service.calculateRate(request))
}
