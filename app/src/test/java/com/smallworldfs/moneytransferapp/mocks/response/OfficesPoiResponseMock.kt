package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.offices.model.Location
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesPoiResponse

object OfficesPoiResponseMock {

    private val location = Location()

    val officesPoiResponse = OfficesPoiResponse(
        listOf(location, location),
        "msg"
    )
}
