package com.smallworldfs.moneytransferapp.data.login.network

import com.smallworldfs.moneytransferapp.data.login.model.LimitedLoginRequest
import com.smallworldfs.moneytransferapp.data.login.model.LimitedLoginResponse
import com.smallworldfs.moneytransferapp.data.login.model.LoginRequest
import com.smallworldfs.moneytransferapp.data.login.model.LoginResponse
import com.smallworldfs.moneytransferapp.data.login.model.RegisterCredentialsRequest
import com.smallworldfs.moneytransferapp.data.login.model.RegisterUserRequest
import com.smallworldfs.moneytransferapp.data.login.model.SoftRegisterResponse
import com.smallworldfs.moneytransferapp.data.login.model.UserResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginService {

    @POST(EndPoint.LOGIN)
    fun login(
        @Path("lang") lang: String,
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST(EndPoint.LIMITED_LOGIN)
    fun limitedLogin(
        @Path("lang") lang: String,
        @Body request: LimitedLoginRequest
    ): Call<LimitedLoginResponse>

    @POST(EndPoint.REGISTER_CREDENTIALS_ENDPOINT)
    fun registerCredentials(
        @Path("lang") lang: String,
        @Body params: RegisterCredentialsRequest
    ): Call<SoftRegisterResponse>

    @POST(EndPoint.REGISTER_USER)
    fun registerUser(
        @Path("lang") lang: String,
        @Query("uuid") uuid: String,
        @Query("userToken") userToken: String,
        @Body params: RegisterUserRequest
    ): Call<UserResponse>
}
