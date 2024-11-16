package com.smallworldfs.moneytransferapp.modules.login.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.login.domain.model.AppTokenUserServer
import com.smallworldfs.moneytransferapp.modules.login.domain.model.LoginUserServer
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.AppTokenRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.LogoutRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.ServerLoginRequest
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface LoginService {

    @POST(EndPoint.LOGIN)
    fun loginUser(@QueryMap request: ServerLoginRequest?): Observable<Response<LoginUserServer>?>?

    @POST(EndPoint.APPTOKEN)
    fun appToken(@QueryMap request: AppTokenRequest?): Observable<Response<AppTokenUserServer>?>?

    @POST(EndPoint.LOGOUT)
    fun logout(@QueryMap request: LogoutRequest?): Observable<Response<Void>?>
}
