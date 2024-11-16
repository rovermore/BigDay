package com.smallworldfs.moneytransferapp.modules.register.domain.repository

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.modules.register.domain.service.SoftRegisterNetworkDatasource
import retrofit2.Response
import rx.Single
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val softRegisterNetworkDatasource: SoftRegisterNetworkDatasource
) {
    fun getToken(): Single<Response<ResponseOAuthTokenDataModel>> {
        return softRegisterNetworkDatasource.getIntegrity("oauth").flatMap {
            softRegisterNetworkDatasource.refreshAccessToken(RequestOAuthTokenDataModel(it))
        }
    }
}
