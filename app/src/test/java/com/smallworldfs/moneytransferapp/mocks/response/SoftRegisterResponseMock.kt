package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.login.model.Data
import com.smallworldfs.moneytransferapp.data.login.model.SoftRegisterResponse
import com.smallworldfs.moneytransferapp.data.login.model.User

object SoftRegisterResponseMock {

    private val userResponse = User(
        42141,
        "uuid",
        "email",
        "name",
        "surname",
        "country",
        "status",
        "userToken",
        "apptoken",
        false
    )

    private val data = Data(userResponse)

    val softRegisterResponse = SoftRegisterResponse(data)
}
