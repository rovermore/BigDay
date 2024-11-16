package com.smallworldfs.moneytransferapp.presentation.autentix.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

open class AutentixMessageUIModel

data class AutentixSuccessMessageUIModel(
    private val value: String = STRING_EMPTY
) : AutentixMessageUIModel()

data class AutentixIgnoredEventUIModel(
    private val message: String = STRING_EMPTY
) : AutentixMessageUIModel()

sealed class AutentixErrorMessageUIModel : AutentixMessageUIModel() {

    object AutentixServerError : AutentixErrorMessageUIModel()
    object AutentixClientError : AutentixErrorMessageUIModel()
    object AutentixTokenInvalidError : AutentixErrorMessageUIModel()
    object AutentixTokenExpiredError : AutentixErrorMessageUIModel()
    object AutentixUnknownError : AutentixErrorMessageUIModel()
    object AutentixSessionInactivityError : AutentixErrorMessageUIModel()
    object AutentixUserInactivityError : AutentixErrorMessageUIModel()
    object AutentixUserCancelActionError : AutentixErrorMessageUIModel()
    object AutentixCameraPermissionError : AutentixErrorMessageUIModel()
    object AutentixCameraOpenError : AutentixErrorMessageUIModel()
    object AutentixFileTypeError : AutentixErrorMessageUIModel()
    object AutentixFileSizeError : AutentixErrorMessageUIModel()
    object AutentixSdkError : AutentixErrorMessageUIModel()
    object AutentixSlowConnectionError : AutentixErrorMessageUIModel()
    object AutentixCameraDeniedError : AutentixErrorMessageUIModel()
}
