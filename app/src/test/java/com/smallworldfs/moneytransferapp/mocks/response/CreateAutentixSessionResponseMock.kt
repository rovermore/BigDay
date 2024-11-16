package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.autentix.network.model.CreateAutentixSessionResponse

object CreateAutentixSessionResponseMock {

    val createAutentixSessionResponse = CreateAutentixSessionResponse(
        "url",
        "externalId",
        20000L
    )
}
