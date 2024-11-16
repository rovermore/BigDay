package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.address.model.AddressResponse

object AddressResponseMock {

    private val detail = AddressResponse.Detail("text", "description")

    private val address = AddressResponse.Address(
        "id",
        "type",
        detail
    )

    val addressResponse = AddressResponse(listOf(address, address))
}
