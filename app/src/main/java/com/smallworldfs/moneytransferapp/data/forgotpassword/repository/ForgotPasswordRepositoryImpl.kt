package com.smallworldfs.moneytransferapp.data.forgotpassword.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.forgotpassword.network.ForgotPasswordNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.repository.ForgotPasswordRepository
import javax.inject.Inject

class ForgotPasswordRepositoryImpl @Inject constructor(
    private val forgotPasswordNetworkDatasource: ForgotPasswordNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
) : ForgotPasswordRepository {

    override fun requestForgotPassword(email: String, country: String): OperationResult<Boolean, Error> =
        forgotPasswordNetworkDatasource.requestForgotPassword(email, country)
            .map { it.msg == "success" }
            .mapFailure { apiErrorMapper.map(it) }
}
