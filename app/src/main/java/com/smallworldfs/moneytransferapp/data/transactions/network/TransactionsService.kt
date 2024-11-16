package com.smallworldfs.moneytransferapp.data.transactions.network

import com.smallworldfs.moneytransferapp.data.transactions.model.MyActivityResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionsService {

    @GET(EndPoint.USER_ACTIVITY)
    fun requestUserTransactions(@Query("userId") userId: String, @Query("userToken") userToken: String, @Query("type") type: String): Call<MyActivityResponse>
}
