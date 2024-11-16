package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.address.model.AddressFormResponse
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

object AddressFormResponseMock {

    private val field = Field(
        "type",
        "subtype",
        "value"
    )

    private val data = AddressFormResponse.Data(listOf(field, field))

    val addressFormResponse = AddressFormResponse(data)
}
