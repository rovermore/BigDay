package com.smallworldfs.moneytransferapp.data.base.network.models

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldError
import javax.inject.Inject

class APIErrorMapper @Inject constructor() {

    fun map(apiError: APIError): Error = when (apiError) {
        is APIError.InternalServerError -> Error.UncompletedOperation(apiError.message)
        is APIError.ServiceUnavailable -> Error.UncompletedOperation(apiError.message)
        is APIError.Forbidden -> if (apiError.subCode.isNotEmpty()) mapSubCode(apiError) else Error.Unauthorized()
        is APIError.Unauthorized -> mapSubCode(apiError)
        is APIError.BadRequest -> mapSubCode(apiError)
        is APIError.NotFound -> mapSubCode(apiError)
        is APIError.TimeOut -> Error.ConnectionError(apiError.message)
        is APIError.UnprocessableEntity -> Error.EntityValidationError(apiError.response.validation.exceptionMessage, apiError.response.validation.fields.map { FieldError(it.field, it.errors) })
        else -> Error.Unmapped(apiError.message)
    }

    private fun mapSubCode(apiError: APIError): Error = when (apiError.subCode) {
        "e123" -> Error.NameIncorrectLength(apiError.message)
        "e122" -> Error.PasswordIncorrectLength(apiError.message)
        "e121" -> Error.EmailFieldNotValidType(apiError.message)
        "e120" -> Error.EmailIncorrectLength(apiError.message)

        "e108" -> Error.UserAlreadyRegistered(apiError.message)
        "e107" -> Error.UserStatusNotPdtProfileForHardRegister(apiError.message)
        "e106" -> Error.NoUserInDataBase(apiError.message)
        "e105" -> Error.LoginCredentials(apiError.message)
        "e104" -> Error.UserNotAvailableForTransactions(apiError.message)
        "e103" -> Error.UserNotAvailable(apiError.message)
        "e102" -> Error.StateBlocked(apiError.message)
        "e101" -> Error.EmailScoreNotPassed(apiError.message)
        "e100" -> Error.IncorrectCredentials(apiError.message)
        "e724" -> Error.DocumentNotFound(apiError.message)
        "e413" -> Error.InvalidPhoneCode(apiError.message)
        "e411" -> Error.AddressNotFound(apiError.message)
        "e412" -> Error.OTPNotFound(apiError.message)
        "e415" -> Error.SecurityErrorForbidden(apiError.message)
        "e610", "e611", "e612" -> Error.SecurityErrorInvalidIntegrity(apiError.message)

        else -> Error.UncompletedOperation(apiError.message)
    }
}
