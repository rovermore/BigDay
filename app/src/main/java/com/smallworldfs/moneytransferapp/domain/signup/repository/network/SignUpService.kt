package com.smallworldfs.moneytransferapp.domain.signup.repository.network

import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordResponse
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface SignUpService {

    @GET(EndPoint.REGISTER_FORM)
    fun requestSignUpForm(@QueryMap request: SignUpRequest): Observable<SignUpResponse>

    @POST(EndPoint.UPDATE_PASSWORD)
    fun requestChangePassword(@QueryMap request: ChangePasswordRequest): Observable<ChangePasswordResponse>
}
