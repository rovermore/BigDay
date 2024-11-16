package com.smallworldfs.moneytransferapp.data.operations.network

import com.smallworldfs.moneytransferapp.data.operations.model.OTPResponse
import com.smallworldfs.moneytransferapp.data.operations.model.SendOTPRequest
import com.smallworldfs.moneytransferapp.data.operations.model.ValidateOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationsService {

    @POST("v4/{lang}/authn/otp/{otpId}/verify")
    fun validateOTP(
        @Path("lang") lang: String,
        @Path("otpId") operationId: String,
        @Query("uuid") uuid: String,
        @Query("userToken") userToken: String,
        @Body body: ValidateOtpRequest
    ): Call<OTPResponse>

    @POST("v4/{lang}/authn/otp")
    fun sendOTP(
        @Path("lang") lang: String,
        @Query("uuid") uuid: String,
        @Query("userToken") userToken: String,
        @Body body: SendOTPRequest
    ): Call<OTPResponse>

    @POST("v4/{lang}/authn/otp/{otpId}/resend")
    fun resendOTP(
        @Path("lang") lang: String,
        @Path("otpId") operationId: String,
        @Query("uuid") uuid: String,
        @Query("userToken") userToken: String,
        @Body body: SendOTPRequest,
    ): Call<OTPResponse>
}
