package com.smallworldfs.moneytransferapp.data.userdata.network

import com.smallworldfs.moneytransferapp.data.login.model.UserResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserDataService {

    @GET(EndPoint.USER)
    fun getUser(
        @Path("lang") lang: String,
        @Path("uuid") uuid: String,
        @Query("userToken") userToken: String
    ): Call<UserResponse>
}
