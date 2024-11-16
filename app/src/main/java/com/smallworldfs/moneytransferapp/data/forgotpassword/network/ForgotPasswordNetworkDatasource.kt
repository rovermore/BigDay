package com.smallworldfs.moneytransferapp.data.forgotpassword.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.forgotpassword.model.ForgotPasswordResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class ForgotPasswordNetworkDatasource(
    private val forgotPasswordService: ForgotPasswordService,
) : NetworkDatasource() {

    fun requestForgotPassword(email: String, country: String): OperationResult<ForgotPasswordResponse, APIError> =
        executeCall(forgotPasswordService.forgotPassword(email, country))
}
