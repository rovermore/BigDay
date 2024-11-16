package com.smallworldfs.moneytransferapp.data.forgotpassword.network

import com.smallworldfs.moneytransferapp.data.forgotpassword.model.ForgotPasswordResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ForgotPasswordService {

    @POST(EndPoint.FORGOT_PASSWORD)
    fun forgotPassword(
        @Query("email") email: String,
        @Query("country") country: String,
    ): Call<ForgotPasswordResponse>
}
