package com.smallworldfs.moneytransferapp.data.promotions.network

import com.smallworldfs.moneytransferapp.data.promotions.model.PromotionResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.PromotionsResponse
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.ServerPromotionsRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

interface PromotionsService {

    // TODO: Remove this when migrate SendToFragment
    @GET(EndPoint.PROMOTIONS)
    fun getPromotions(
        @QueryMap request: ServerPromotionsRequest
    ): Observable<Response<PromotionsResponse>?>?

    @GET(EndPoint.PROMOTIONS)
    fun getPromotions(
        @Query("countryOrigin") originCountry: String,
        @Query("countryDestination") payoutCountry: String,
        @Query("clientId") clientId: String
    ): Call<PromotionResponse>
}
