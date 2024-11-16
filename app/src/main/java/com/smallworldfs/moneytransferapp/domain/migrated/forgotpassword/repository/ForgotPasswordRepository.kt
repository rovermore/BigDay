package com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface ForgotPasswordRepository {

    fun requestForgotPassword(email: String, country: String): OperationResult<Boolean, Error>
}
