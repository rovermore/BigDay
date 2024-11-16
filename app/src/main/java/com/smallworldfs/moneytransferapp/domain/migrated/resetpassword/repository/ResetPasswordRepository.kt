package com.smallworldfs.moneytransferapp.domain.migrated.resetpassword.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form

interface ResetPasswordRepository {

    fun requestConfirmResetPassword(password: String, token: String): OperationResult<Boolean, Error>
    fun getForm(bearer: String, country: String): OperationResult<Form, Error>
}
