package com.smallworldfs.moneytransferapp.data.resetpassword.network

import com.smallworldfs.moneytransferapp.data.resetpassword.model.ResetPasswordResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.register.domain.model.FormRegisterServer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ResetPasswordService {

    @POST(EndPoint.RESET_PASSWORD)
    fun resetPassword(
        @Query("password") password: String,
        @Query("token") token: String
    ): Call<ResetPasswordResponse>

    @GET(EndPoint.REGISTER_FORM)
    fun getForm(
        @Header("Authorization") bearer: String,
        @Query("country") country: String,
        @Query("soft") soft: String
    ): Call<FormRegisterServer>
}
