package com.smallworldfs.moneytransferapp.domain.resetpassword.usecase

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.resetpassword.repository.ResetPasswordRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import com.smallworldfs.moneytransferapp.modules.register.domain.repository.RegisterRepository
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val resetPasswordRepository: ResetPasswordRepository,
    private val registerRepository: RegisterRepository,
    private val localeRepository: LocaleRepository,
    private val userDataRepository: UserDataRepository
) {

    fun resetPassword(password: String, token: String): OperationResult<Boolean, Error> =
        resetPasswordRepository.requestConfirmResetPassword(password, token)
            .map { result ->
                if (result) {
                    userDataRepository.clearUserData()
                }
                result
            }

    fun getForm(): OperationResult<Form, Error> =
        resetPasswordRepository.getForm(getBearer(), localeRepository.getLocale())

    private fun getBearer(): String {
        var token = STRING_EMPTY
        registerRepository.getToken().map {
            token = it.body()?.getOAuthToken() ?: STRING_EMPTY
        }
        return token
    }
}
