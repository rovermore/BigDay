package com.smallworldfs.moneytransferapp.modules.register.domain.service

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityDataResponse
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityNonceRequest
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Single

interface SoftRegisterService {

    @POST(EndPoint.OAUTH_ACCESS_TOKEN)
    fun refreshAccessToken(@Body OAuthRequest: RequestOAuthTokenDataModel): Single<Response<ResponseOAuthTokenDataModel>?>?

    @POST(EndPoint.SECURITY_NONCE)
    fun getSecurityNonce(
        @Body operation: IntegrityNonceRequest,
    ): Single<Response<IntegrityDataResponse>?>?
}
