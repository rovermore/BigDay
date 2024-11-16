package com.smallworldfs.moneytransferapp.domain.signup.repository.network

import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordResponse
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpResponse
import io.reactivex.Observable
import javax.inject.Inject

class SignUpNetwork @Inject constructor(private val service: SignUpService) {

    fun requestSignUpForm(request: SignUpRequest): Observable<SignUpResponse> = service.requestSignUpForm(request)

    fun requestChangePassword(request: ChangePasswordRequest): Observable<ChangePasswordResponse> = service.requestChangePassword(request)
}
