package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.base.data.net.api.ApiException
import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.domain.signup.model.ChangePasswordRequest
import com.smallworldfs.moneytransferapp.domain.signup.repository.SignUpRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordChangePasswordUseCase @Inject constructor(
    private val changePasswordGetUserUseCaseContract: ChangePasswordGetUserUseCaseContract,
    private val base64Tool: Base64Tool,
    private val signUpRepository: SignUpRepository,
    private val changePasswordSaveNewPasswordUseCaseContract: ChangePasswordSaveNewPasswordUseCaseContract
) : ChangePasswordChangePasswordUseCaseContract {

    override fun changePassword(oldPassword: String, newPassword: String): Single<Unit> = changePasswordGetUserUseCaseContract.getCurrentUser()
        .flatMap { user ->
            val userId = user.id ?: ""
            val userToken = user.userToken ?: ""
            val encodedPassword = base64Tool.encode(oldPassword)
            val encodedNewPassword = base64Tool.encode(newPassword)
            val request = ChangePasswordRequest(encodedPassword, encodedNewPassword, userId, userToken)
            signUpRepository.requestChangePassword(request).firstOrError()
                .flatMap { changePasswordResponse ->
                    if (changePasswordResponse.msg == "success") {
                        changePasswordSaveNewPasswordUseCaseContract.saveNewPassword(newPassword)
                    } else {
                        Single.error(ApiException())
                    }
                }
        }
        .subscribeOn(Schedulers.io())
}

interface ChangePasswordChangePasswordUseCaseContract {
    fun changePassword(oldPassword: String, newPassword: String): Single<Unit>
}
