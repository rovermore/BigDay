package com.smallworldfs.moneytransferapp.modules.oauth.domain.service

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.oauth.repository.network.OAuthService
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import retrofit2.Call

class OAuthNetworkDatasource(
    private val service: OAuthService
) : NetworkDatasource() {

    fun getSyncAccessToken(request: RequestOAuthTokenDataModel): OperationResult<ResponseOAuthTokenDataModel, APIError> =
        executeCall(service.refreshAccessToken(request))

    fun getSyncAccessTokenLegacy(request: RequestOAuthTokenDataModel): Call<ResponseOAuthTokenDataModel> =
        service.refreshAccessToken(request)
}
