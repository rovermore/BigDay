package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.login.model.LoginResponse

object LoginResponseMock {

    private val userResponse = LoginResponse.Data.UserResponse(
        42141,
        "uuid",
        "email",
        "name",
        "surname",
        "country",
        "status",
        "userToken",
        false
    )
    private val data = LoginResponse.Data(userResponse)

    val loginResponse = LoginResponse(data)
}
