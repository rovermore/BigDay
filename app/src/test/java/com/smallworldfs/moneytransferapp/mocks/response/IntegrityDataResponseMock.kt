package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityDataResponse
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityNonceResponse

object IntegrityDataResponseMock {

    private val integrityNonceResponse = IntegrityNonceResponse("nonce")

    val integrityDataResponse = IntegrityDataResponse(integrityNonceResponse, "signature")
}
