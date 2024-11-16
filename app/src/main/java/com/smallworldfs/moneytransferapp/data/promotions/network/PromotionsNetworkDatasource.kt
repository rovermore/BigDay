package com.smallworldfs.moneytransferapp.data.promotions.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.promotions.model.PromotionResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.PromotionsResponse
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.ServerPromotionsRequest
import retrofit2.Response
import rx.Observable

class PromotionsNetworkDatasource(
    private val service: PromotionsService
) : NetworkDatasource() {

    // TODO: Remove this when migrate SendToFragment
    fun requestPromotions(request: ServerPromotionsRequest): Observable<Response<PromotionsResponse>> =
        executeCall(service.getPromotions(request))

    fun requestPromotions(originCountry: String, payoutCountry: String, clientId: String): OperationResult<PromotionResponse, APIError> =
        executeCall(service.getPromotions(originCountry, payoutCountry, clientId))
}
