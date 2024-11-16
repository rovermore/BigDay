package com.smallworldfs.moneytransferapp.data.integrity.network

import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityDataResponse
import com.smallworldfs.moneytransferapp.data.integrity.model.IntegrityNonceRequest
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IntegrityService {

    @POST(EndPoint.SECURITY_NONCE)
    fun getSecurityNonce(
        @Body operation: IntegrityNonceRequest,
    ): Call<IntegrityDataResponse>
}
