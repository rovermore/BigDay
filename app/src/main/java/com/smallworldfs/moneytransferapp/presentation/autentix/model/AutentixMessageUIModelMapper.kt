package com.smallworldfs.moneytransferapp.presentation.autentix.model

import javax.inject.Inject

class AutentixMessageUIModelMapper @Inject constructor() {

    fun map(autentixMessage: AutentixMessage): AutentixMessageUIModel =
        with(autentixMessage.payload) {
            when (value) {
                "success" -> AutentixSuccessMessageUIModel(value)
                "4000" -> AutentixErrorMessageUIModel.AutentixCameraPermissionError
                "4003" -> AutentixErrorMessageUIModel.AutentixUserCancelActionError
                "4004" -> AutentixErrorMessageUIModel.AutentixSessionInactivityError
                "4005" -> AutentixErrorMessageUIModel.AutentixUnknownError
                "4010" -> AutentixErrorMessageUIModel.AutentixTokenInvalidError
                "4011" -> AutentixErrorMessageUIModel.AutentixTokenExpiredError
                "5000" -> AutentixErrorMessageUIModel.AutentixServerError
                "5001" -> AutentixErrorMessageUIModel.AutentixClientError
                "6000" -> AutentixErrorMessageUIModel.AutentixCameraOpenError
                "6001" -> AutentixErrorMessageUIModel.AutentixUserInactivityError
                "6002" -> AutentixErrorMessageUIModel.AutentixFileTypeError
                "6003" -> AutentixErrorMessageUIModel.AutentixFileSizeError
                "6004" -> AutentixErrorMessageUIModel.AutentixSdkError
                "6005" -> AutentixErrorMessageUIModel.AutentixSlowConnectionError
                "6006" -> AutentixErrorMessageUIModel.AutentixCameraDeniedError
                else -> AutentixIgnoredEventUIModel(message)
            }
        }
}
