package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.offices.model.Branches
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesResponse

object OfficesResponseMock {

    private val branch = Branches()

    val officesResponse = OfficesResponse(
        listOf(branch, branch),
        "msg"
    )
}
