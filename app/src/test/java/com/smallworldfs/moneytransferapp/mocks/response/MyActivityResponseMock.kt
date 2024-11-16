package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.transactions.model.MyActivityResponse
import com.smallworldfs.moneytransferapp.data.transactions.model.TransactionsInfo
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction

object MyActivityResponseMock {

    private val transactionsInfo = TransactionsInfo(
        90.09,
        0.983,
        23
    )

    val myActivityResponse = MyActivityResponse(
        "msg",
        arrayListOf(Transaction(), Transaction()),
        transactionsInfo,
        "cancellation message"
    )
}
