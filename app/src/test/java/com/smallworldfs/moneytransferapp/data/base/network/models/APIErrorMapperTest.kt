package com.smallworldfs.moneytransferapp.data.base.network.models

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldError
import com.smallworldfs.moneytransferapp.mocks.response.ValidationErrorResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class APIErrorMapperTest {

    lateinit var apiErrorMapper: APIErrorMapper

    private val message = "default message"
    private val validationErrorResponse = ValidationErrorResponseMock.validationErrorResponse

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        apiErrorMapper = APIErrorMapper()
    }

    @Test
    fun `when APIError InternalServerError is mapped returns Error UncompletedOperation`() {
        val result = apiErrorMapper.map(APIError.InternalServerError(message))
        Assert.assertEquals(
            Error.UncompletedOperation(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError InternalServerError is mapped returns same message`() {
        val result = apiErrorMapper.map(APIError.InternalServerError(message))
        Assert.assertEquals(APIError.InternalServerError(message).message, result.message)
    }

    @Test
    fun `when APIError ServiceUnavailable is mapped returns Error UncompletedOperation`() {
        val result = apiErrorMapper.map(APIError.ServiceUnavailable(message))
        Assert.assertEquals(
            Error.UncompletedOperation(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError ServiceUnavailable is mapped returns same message`() {
        val result = apiErrorMapper.map(APIError.ServiceUnavailable(message))
        Assert.assertEquals(APIError.ServiceUnavailable(message).message, result.message)
    }

    @Test
    fun `when APIError Forbidden is mapped returns subCode Error`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e102"
            )
        )
        val result = apiErrorMapper.map(APIError.Forbidden(errorResponse))
        Assert.assertEquals(
            Error.StateBlocked(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError Forbidden is mapped returns same message`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e102"
            )
        )
        val result = apiErrorMapper.map(APIError.Forbidden(errorResponse))
        Assert.assertEquals(APIError.Forbidden(errorResponse).message, result.message)
    }

    @Test
    fun `when APIError Unauthorized is mapped returns subCode Error`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e610"
            )
        )
        val result = apiErrorMapper.map(APIError.Unauthorized(errorResponse))
        Assert.assertEquals(
            Error.SecurityErrorInvalidIntegrity(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError Unauthorized is mapped returns same message`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e610"
            )
        )
        val result = apiErrorMapper.map(APIError.Unauthorized(errorResponse))
        Assert.assertEquals(APIError.Unauthorized(errorResponse).message, result.message)
    }

    @Test
    fun `when APIError BadRequest is mapped returns subCode Error`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e413"
            )
        )
        val result = apiErrorMapper.map(APIError.BadRequest(errorResponse))
        Assert.assertEquals(
            Error.InvalidPhoneCode(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError BadRequest is mapped returns same message`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e413"
            )
        )
        val result = apiErrorMapper.map(APIError.BadRequest(errorResponse))
        Assert.assertEquals(APIError.BadRequest(errorResponse).message, result.message)
    }

    @Test
    fun `when APIError NotFound is mapped returns subCode Error`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e106"
            )
        )
        val result = apiErrorMapper.map(APIError.NotFound(errorResponse))
        Assert.assertEquals(
            Error.NoUserInDataBase(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError NotFound is mapped returns same message`() {
        val errorResponse = ErrorResponse(
            ErrorResponse.Error(
                "exceptionMessage",
                message,
                "e106"
            )
        )
        val result = apiErrorMapper.map(APIError.NotFound(errorResponse))
        Assert.assertEquals(APIError.NotFound(errorResponse).message, result.message)
    }

    @Test
    fun `when APIError TimeOut is mapped returns Error ConnectionError`() {
        val result = apiErrorMapper.map(APIError.TimeOut(message))
        Assert.assertEquals(
            Error.ConnectionError(message).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError TimeOut is mapped returns same message`() {
        val result = apiErrorMapper.map(APIError.TimeOut(message))
        Assert.assertEquals(APIError.TimeOut(message).message, result.message)
    }

    @Test
    fun `when APIError UnprocessableEntity is mapped returns Error EntityValidationError`() {
        val result = apiErrorMapper.map(APIError.UnprocessableEntity(message, validationErrorResponse))
        Assert.assertEquals(
            Error.EntityValidationError(
                validationErrorResponse.validation.exceptionMessage,
                validationErrorResponse.validation.fields.map { FieldError(it.field, it.errors) },

            ).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when APIError UnprocessableEntity is mapped returns same message`() {
        val result = apiErrorMapper.map(APIError.UnprocessableEntity(message, validationErrorResponse))
        Assert.assertEquals(
            APIError.UnprocessableEntity(message, validationErrorResponse).response.validation.exceptionMessage,
            result.message
        )
    }
}
