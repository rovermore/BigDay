package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.login.model.UserResponse
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

object UserResponseMock {

    private val user = UserResponse.User(
        1,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        true,
        STRING_EMPTY,
        1,
        Gdpr(),
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY,
        false,
        STRING_EMPTY,
        STRING_EMPTY,
        STRING_EMPTY
    )

    private val data = UserResponse.Data(user)

    val userResponse = UserResponse(data)
}
