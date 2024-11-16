package com.smallworldfs.moneytransferapp.data.calculator.network

import com.smallworldfs.moneytransferapp.data.calculator.model.CalculateRateResponse
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.DeliveryMethodsResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface CalculatorService {

    @GET(EndPoint.DELIVERY_METHODS)
    fun getDeliveryMethods(): Call<DeliveryMethodsResponse>

    @POST(EndPoint.CALCULATE)
    fun calculateRate(@QueryMap request: Map<String, String>): Call<CalculateRateResponse>
}
