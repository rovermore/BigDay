package com.smallworldfs.moneytransferapp.presentation.base

import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterStep

sealed class ErrorType(val message: String = "") {
    object None : ErrorType()
    class GenericError(msg: String) : ErrorType(msg)
    class UnauthorizedError(msg: String) : ErrorType(msg)
    class UnmappedError(msg: String) : ErrorType(msg)
    class ConnectionError(msg: String) : ErrorType(msg)
    class UnregisteredUser(msg: String) : ErrorType(msg)
    class PartiallyRegisteredUser(val step: RegisterStep) : ErrorType()
    class SaveDataError(msg: String) : ErrorType(msg)
    class ReadDataError(msg: String) : ErrorType(msg)
    class DeleteDataError(msg: String) : ErrorType(msg)
    class WrongPasscodeError(msg: String) : ErrorType(msg)
    class ExistingAccount(msg: String) : ErrorType(msg)
    class LoginCredentials(msg: String) : ErrorType(msg)
    class UserNotAvailable(msg: String) : ErrorType(msg)
    class InvalidPhoneCode(msg: String) : ErrorType(msg)
    class EmailScoreNotPassed(msg: String) : ErrorType(msg)
    class EntityValidationError(msg: String, val fields: List<FieldError>) : ErrorType(msg)
    class IncorrectCredentials(msg: String) : ErrorType(msg)
    class StateBlocked(msg: String) : ErrorType(msg)
    class DataNotFound(msg: String) : ErrorType(msg)
    object SecurityErrorForbidden : ErrorType()
    object SecurityErrorInvalidIntegrity : ErrorType()
    object PermissionsNotGranted : ErrorType()
    class FieldError(val field: String, val error: List<String> = emptyList()) : ErrorType(field)
}
