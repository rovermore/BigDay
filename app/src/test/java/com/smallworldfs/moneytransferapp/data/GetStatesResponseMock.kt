package com.smallworldfs.moneytransferapp.data

import com.smallworldfs.moneytransferapp.data.countries.model.GetStatesResponse

object GetStatesResponseMock {

    private val logo = GetStatesResponse.Logo("logoURL")

    private val stateResponse1 = GetStatesResponse.State(
        "code",
        "name",
        logo,
        true
    )

    private val stateResponse2 = GetStatesResponse.State(
        "code",
        "name",
        logo,
        true
    )

    private val dataStates = GetStatesResponse.Data(listOf(stateResponse1, stateResponse2))

    val statesResponse = GetStatesResponse(dataStates)
}
