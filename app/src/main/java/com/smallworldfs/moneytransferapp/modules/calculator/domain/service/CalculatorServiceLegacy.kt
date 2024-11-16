package com.smallworldfs.moneytransferapp.modules.calculator.domain.service

import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.DeliveryMethodsResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCalculateRequest
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerDeliveryMethodsRequest
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface CalculatorServiceLegacy {

    @GET(EndPoint.PAYMENTMETHODS)
    fun getCurrencies(@QueryMap request: ServerCurrencieRequest?): Observable<Response<CurrenciesResponse>?>?

    @POST(EndPoint.CALCULATE)
    fun calculate(@QueryMap request: ServerCalculateRequest?): Observable<Response<RateResponse>?>?

    @GET(EndPoint.DELIVERY_METHODS)
    fun getDeliveryMethods(@QueryMap request: ServerDeliveryMethodsRequest?): Observable<Response<DeliveryMethodsResponse>?>?
}
