package com.smallworldfs.moneytransferapp.modules.c2b.domain.service

import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.server.C2BRequest
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

interface C2BService {

    @GET(EndPoint.BENEFICIARY_TYPES_C2B)
    fun getBeneficiaryTypes(
        @QueryMap request: C2BRequest?
    ): Observable<Response<C2BResponse>?>?
}
