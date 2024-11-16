package com.smallworldfs.moneytransferapp.domain.signup.repository

import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordResponse
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpRequest
import com.smallworldfs.moneytransferapp.domain.signup.model.SignUpResponse
import com.smallworldfs.moneytransferapp.domain.signup.repository.network.SignUpNetwork
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.forms.Type
import io.reactivex.Observable
import javax.inject.Inject

class SignUpRepository @Inject constructor() {

    @Inject
    lateinit var signUpNetwork: SignUpNetwork

    fun requestSignUpForm(request: SignUpRequest): Observable<SignUpResponse> =
        signUpNetwork.requestSignUpForm(request)

    fun requestPasswordAttributes(country: String): Observable<Attributes> =
        signUpNetwork.requestSignUpForm(SignUpRequest(country))
            .flatMap { response ->
                var attributes = Attributes()
                response.form?.let { form ->
                    form.fields?.let { fields ->
                        for (field: Field in fields) {
                            if (field.type == Type.PASSWORD) {
                                attributes = field.attributes ?: Attributes()
                            }
                        }
                    }
                }
                Observable.just(attributes)
            }

    fun requestChangePassword(request: ChangePasswordRequest): Observable<ChangePasswordResponse> =
        signUpNetwork.requestChangePassword(request)
}
