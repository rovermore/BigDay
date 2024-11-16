package com.smallworldfs.moneytransferapp.modules.flinks.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.FlinksResponse
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.server.FlinksRequest
import retrofit2.Response
import rx.Observable

class FlinksNetworkDatasource(
    private val service: FlinksService
) : NetworkDatasource() {

    fun getFlinksInfo(request: FlinksRequest): Observable<Response<FlinksResponse>> =
        executeCall(service.getFlinksInfo(request))

    fun postVerifyFlinksState(request: FlinksRequest): Observable<Response<FlinksResponse>> =
        executeCall(service.verifyFlinksState(request))
}
