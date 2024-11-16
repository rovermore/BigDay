package com.smallworldfs.moneytransferapp.domain.token.repository.network

import com.smallworldfs.moneytransferapp.domain.token.model.AppTokenRequest
import com.smallworldfs.moneytransferapp.domain.token.model.AppTokenResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AppTokenService {

    @POST(EndPoint.APPTOKEN)
    fun appToken(@QueryMap request: AppTokenRequest): Observable<AppTokenResponse>
}
