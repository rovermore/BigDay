package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.domain.signup.repository.SignUpRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordRequestPasswordAttributesUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val changePasswordGetUserUseCaseContract: ChangePasswordGetUserUseCaseContract
) : ChangePasswordRequestPasswordAttributesUseCaseContract {

    override fun requestPasswordAttributes(): Single<Attributes> = changePasswordGetUserUseCaseContract.getCurrentUser()
        .flatMap { user ->
            signUpRepository.requestPasswordAttributes(user.country.firstKey()).firstOrError()
        }
        .subscribeOn(Schedulers.io())
}

interface ChangePasswordRequestPasswordAttributesUseCaseContract {
    fun requestPasswordAttributes(): Single<Attributes>
}
