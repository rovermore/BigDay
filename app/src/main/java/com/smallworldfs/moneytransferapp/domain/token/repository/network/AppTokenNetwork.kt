package com.smallworldfs.moneytransferapp.domain.token.repository.network

import com.smallworldfs.moneytransferapp.domain.token.model.AppTokenRequest
import com.smallworldfs.moneytransferapp.domain.token.model.AppTokenResponse
import io.reactivex.Observable
import javax.inject.Inject

class AppTokenNetwork @Inject constructor(private val service: AppTokenService) {

    fun requestAppToken(request: AppTokenRequest): Observable<AppTokenResponse> = service.appToken(request)
}
