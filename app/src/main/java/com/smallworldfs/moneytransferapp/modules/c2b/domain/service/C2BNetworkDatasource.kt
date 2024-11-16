package com.smallworldfs.moneytransferapp.modules.c2b.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.server.C2BRequest
import retrofit2.Response
import rx.Observable

class C2BNetworkDatasource(
    private val service: C2BService
) : NetworkDatasource() {

    fun requestBeneficiaryTypes(request: C2BRequest): Observable<Response<C2BResponse>> =
        executeCall(service.getBeneficiaryTypes(request))
}
