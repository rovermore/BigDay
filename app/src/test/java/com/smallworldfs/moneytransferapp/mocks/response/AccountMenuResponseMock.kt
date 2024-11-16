package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.account.account.model.AccountMenuItemResponse
import com.smallworldfs.moneytransferapp.data.account.account.model.AccountMenuResponse
import com.smallworldfs.moneytransferapp.data.account.account.model.Data

object AccountMenuResponseMock {

    private val accountMenuItemResponse =
        AccountMenuItemResponse(
            "block",
            "4",
            "coasas",
            "descripcion",
            true,
            2,
            4
        )

    private val data = Data(
        mutableListOf(accountMenuItemResponse, accountMenuItemResponse),
        mutableListOf(accountMenuItemResponse, accountMenuItemResponse)
    )

    val accountMenuResponse = AccountMenuResponse(data)
}
