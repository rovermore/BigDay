package com.smallworldfs.moneytransferapp.data.base.network.models

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

sealed class APIError(val code: Int, val message: String, val subCode: String = STRING_EMPTY) {
    class InternalServerError(message: String) : APIError(500, message)
    class ServiceUnavailable(message: String) : APIError(503, message)
    class UnprocessableEntity(message: String, val response: ValidationErrorResponse) : APIError(422, message)
    class NotFound(val response: ErrorResponse) : APIError(404, response.error.message, response.error.subCode)
    class Forbidden(val response: ErrorResponse) : APIError(403, response.error.message, response.error.subCode)
    class LoginTimeout(val response: ErrorResponse) : APIError(440, response.error.message, response.error.subCode)
    class BadRequest(val response: ErrorResponse) : APIError(400, response.error.message, response.error.subCode)
    class Unauthorized(val response: ErrorResponse) : APIError(401, response.error.message, response.error.subCode)
    class TimeOut(message: String) : APIError(408, message)

    class UnmappedError(code: Int, message: String) : APIError(code, message)
}
