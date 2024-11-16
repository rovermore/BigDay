package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

enum class Error {
    SPLASH_ERROR,
    NO_CONNECTION,
    BAD_RESPONSE,
    UPDATE_APP,
    SESSION_EXPIRED,
    DEVICE_ROOTED,
    LOGIN_INCORRECT,
    CHANGE_PASSWORD,
    UNKNOWN_ERROR,
    INTEGRITY_ERROR,
    FIELD_ERROR
}

enum class ErrorType {
    GENERIC,
    POPUP,
    FORM,
    CUSTOM
}

data class DataError(var title: String = "", var subtitle: String = "", var error: Error, var errorType: ErrorType)
