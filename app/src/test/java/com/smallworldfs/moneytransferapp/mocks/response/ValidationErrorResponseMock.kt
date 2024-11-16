package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.base.network.models.ValidationErrorResponse

object ValidationErrorResponseMock {

    private val field = ValidationErrorResponse.Field("field", listOf("error1", "error2"))

    private val validation = ValidationErrorResponse.Validation(
        listOf(field, field),
        "exception message"
    )

    val validationErrorResponse = ValidationErrorResponse(validation)
}
