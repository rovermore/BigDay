package com.smallworldfs.moneytransferapp.data.resetpassword.network

import android.util.Base64
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.resetpassword.model.ResetPasswordResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.register.domain.model.FormRegisterServer
import javax.inject.Inject

class ResetPasswordNetworkDatasource @Inject constructor(
    private val resetPasswordService: ResetPasswordService
) : NetworkDatasource() {

    fun resetPasswordConfirm(password: String, token: String): OperationResult<ResetPasswordResponse, APIError> {
        val passwordEncoded = Base64.encodeToString(password.toByteArray(), Base64.DEFAULT)
        return executeCall(resetPasswordService.resetPassword(passwordEncoded, token))
    }

    fun getFormRegister(bearer: String, country: String): OperationResult<FormRegisterServer, APIError> =
        executeCall(resetPasswordService.getForm(bearer, country, "1"))
}
