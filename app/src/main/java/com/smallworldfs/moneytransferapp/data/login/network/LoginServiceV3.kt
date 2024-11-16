package com.smallworldfs.moneytransferapp.data.login.network

import com.smallworldfs.moneytransferapp.data.userdata.model.LogoutResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginServiceV3 {

    @POST(EndPoint.LOGOUT)
    fun logout(
        @Query("userToken") userToken: String,
        @Query("userId") id: String,
    ): Call<LogoutResponse>
}
