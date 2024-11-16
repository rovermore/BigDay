package com.smallworldfs.moneytransferapp.presentation.softregister.model

import javax.inject.Inject

class RegisterStepMapper @Inject constructor() {

    val PDT_SMS = "PDT_SMS"
    val PDT_PROFILE = "PDT_PROFILE"

    fun map(state: String): RegisterStep =
        when (state) {
            PDT_SMS -> RegisterStep.RegisterPhone
            PDT_PROFILE -> RegisterStep.RegisterData
            else -> RegisterStep.RegisterCredentials
        }
}
