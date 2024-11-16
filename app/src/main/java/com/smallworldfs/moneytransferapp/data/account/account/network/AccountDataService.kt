package com.smallworldfs.moneytransferapp.data.account.account.network

import com.smallworldfs.moneytransferapp.data.account.account.model.AccountMenuResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountDataService {

    @GET(EndPoint.ACCOUNT_MENU)
    fun getAccountMenu(
        @Query("userToken") userToken: String,
        @Query("userId") userId: String,
    ): Call<AccountMenuResponse>
}
