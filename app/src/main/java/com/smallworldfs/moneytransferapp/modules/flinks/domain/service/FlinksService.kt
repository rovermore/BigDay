package com.smallworldfs.moneytransferapp.modules.flinks.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.FlinksResponse
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.server.FlinksRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface FlinksService {

    @GET(EndPoint.FLINKS_ENDPOINT)
    fun getFlinksInfo(@QueryMap request: FlinksRequest?): Observable<Response<FlinksResponse>?>?

    @POST(EndPoint.FLINKS_VERFIY_ENDPOINT)
    fun verifyFlinksState(@QueryMap request: FlinksRequest?): Observable<Response<FlinksResponse>?>?
}
