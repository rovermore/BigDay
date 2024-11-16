package com.smallworldfs.moneytransferapp.data.base.network.models

class ValidationErrorResponse(
    val validation: Validation = Validation()
) {
    data class Validation(
        val fields: List<Field> = emptyList(),
        val exceptionMessage: String = ""
    )

    data class Field(
        val field: String,
        val errors: List<String>
    )
}
