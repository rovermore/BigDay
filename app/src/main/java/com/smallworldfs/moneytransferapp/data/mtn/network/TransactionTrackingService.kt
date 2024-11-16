package com.smallworldfs.moneytransferapp.data.mtn.network

import com.smallworldfs.moneytransferapp.data.mtn.model.MTNRequest
import com.smallworldfs.moneytransferapp.data.mtn.model.TransactionTrackingResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface TransactionTrackingService {

    @GET(EndPoint.TRANSACTION_TRACKING)
    fun trackTransaction(@QueryMap request: MTNRequest?): Call<TransactionTrackingResponse>
}
