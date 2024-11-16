package com.smallworldfs.moneytransferapp.data.oauth.repository.network

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OAuthService {

    @POST(EndPoint.OAUTH_ACCESS_TOKEN)
    fun refreshAccessToken(@Body requestOAuthTokenDataModel: RequestOAuthTokenDataModel): Call<ResponseOAuthTokenDataModel>
}
