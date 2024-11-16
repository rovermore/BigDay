package com.smallworldfs.moneytransferapp.modules.login.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.login.domain.model.LoginUserServer
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.LogoutRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.ServerLoginRequest
import retrofit2.Response
import rx.Observable

class LoginNetworkDatasource(
    private val service: LoginService
) : NetworkDatasource() {

    fun login(request: ServerLoginRequest?): Observable<Response<LoginUserServer>> =
        executeCall(service.loginUser(request))

    fun logout(request: LogoutRequest?): Observable<Response<Void>> =
        executeCall(service.logout(request))
}
