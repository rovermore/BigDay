package com.smallworldfs.moneytransferapp.modules.calculator.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.DeliveryMethodsResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCalculateRequest
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerDeliveryMethodsRequest
import retrofit2.Response
import rx.Observable

class CalculatorNetworkDatasourceLegacy(
    private val service: CalculatorServiceLegacy
) : NetworkDatasource() {

    fun getCurrencies(request: ServerCurrencieRequest): Observable<Response<CurrenciesResponse>> =
        executeCall(service.getCurrencies(request))

    fun calculateRate(request: ServerCalculateRequest): Observable<Response<RateResponse>> =
        executeCall(service.calculate(request))

    fun requestDeliveryMethods(request: ServerDeliveryMethodsRequest): Observable<Response<DeliveryMethodsResponse>> =
        executeCall(service.getDeliveryMethods(request))
}
