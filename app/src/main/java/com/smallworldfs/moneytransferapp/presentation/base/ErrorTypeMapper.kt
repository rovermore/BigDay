package com.smallworldfs.moneytransferapp.presentation.base

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterStep
import javax.inject.Inject

class ErrorTypeMapper @Inject constructor() {
    fun map(error: Error): ErrorType = when (error) {
        is Error.OperationCompletedWithError -> ErrorType.GenericError(error.message)
        is Error.Unmapped -> ErrorType.UnmappedError(error.message)
        is Error.Unauthorized -> ErrorType.UnauthorizedError(error.message)
        is Error.ConnectionError -> ErrorType.ConnectionError(error.message)
        is Error.UnregisteredUser -> ErrorType.UnregisteredUser(error.message)
        is Error.SaveDataError -> ErrorType.SaveDataError(error.message)
        is Error.ReadDataError -> ErrorType.ReadDataError(error.message)
        is Error.DeleteDataError -> ErrorType.DeleteDataError(error.message)
        is Error.UserAlreadyRegistered -> ErrorType.ExistingAccount(error.message)
        is Error.LoginCredentials -> ErrorType.LoginCredentials(error.message)
        is Error.UserNotAvailable -> ErrorType.UserNotAvailable(error.message)
        is Error.InvalidPhoneCode -> ErrorType.InvalidPhoneCode(error.message)
        is Error.EmailScoreNotPassed -> ErrorType.EmailScoreNotPassed(error.message)
        is Error.EntityValidationError -> ErrorType.EntityValidationError(error.message, error.fields.map { ErrorType.FieldError(it.field, it.error) })
        is Error.IncorrectCredentials -> ErrorType.IncorrectCredentials(error.message)
        is Error.PendingPhoneValidation -> ErrorType.PartiallyRegisteredUser(RegisterStep.RegisterPhone)
        is Error.PendingProfileValidation -> ErrorType.PartiallyRegisteredUser(RegisterStep.RegisterData)
        is Error.StateBlocked -> ErrorType.StateBlocked(error.message)
        is Error.AddressNotFound -> ErrorType.DataNotFound(error.message)
        is Error.SecurityErrorForbidden -> ErrorType.SecurityErrorForbidden
        is Error.SecurityErrorInvalidIntegrity -> ErrorType.SecurityErrorInvalidIntegrity
        is Error.NoUserInDataBase -> ErrorType.LoginCredentials(error.message)

        else -> ErrorType.GenericError(error.message)
    }
}
