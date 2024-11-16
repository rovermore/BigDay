package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.modules.status.domain.model.CancelTransactionResponse

object CancelTransactionResponseMock {

    val cancelTransactionSuccessResponse = CancelTransactionResponse("", "").apply { result = true }
    val cancelTransactionFailureResponse = CancelTransactionResponse("", "").apply { result = false }
}
