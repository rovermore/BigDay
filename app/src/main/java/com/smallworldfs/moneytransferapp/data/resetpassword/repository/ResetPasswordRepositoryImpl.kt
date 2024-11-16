package com.smallworldfs.moneytransferapp.data.resetpassword.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.resetpassword.network.ResetPasswordNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.resetpassword.repository.ResetPasswordRepository
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(
    private val resetPasswordNetworkDatasource: ResetPasswordNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper
) : ResetPasswordRepository {

    override fun requestConfirmResetPassword(password: String, token: String): OperationResult<Boolean, Error> =
        resetPasswordNetworkDatasource.resetPasswordConfirm(password, token)
            .map { it.msg == "success" }
            .mapFailure { apiErrorMapper.map(it) }

    override fun getForm(bearer: String, country: String): OperationResult<Form, Error> =
        resetPasswordNetworkDatasource.getFormRegister(bearer, country)
            .map { it.form }
            .mapFailure { apiErrorMapper.map(it) }
}
