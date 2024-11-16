package com.smallworldfs.moneytransferapp.data.account.documents.repository.network

import com.smallworldfs.moneytransferapp.data.autentix.network.model.AutentixSessionStatusResponse
import com.smallworldfs.moneytransferapp.data.autentix.network.model.CreateAutentixSessionResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAuthenticationService {

    @GET(EndPoint.CREATE_AUTENTIX_SESSION)
    fun createAutentixSession(@Path("uuid") uuid: String, @Path("lang") lang: String, @Query("userToken") userToken: String, @Query("documentType") documentType: String, @Query("fileTypes[]") faceCompare: String?): Call<CreateAutentixSessionResponse>

    @GET(EndPoint.AUTENTIX_SESSION_STATUS)
    fun checkAutentixSessionStatus(@Path("uuid") uuid: String, @Path("externalId") externalId: String, @Path("lang") lang: String, @Query("userToken") userToken: String): Call<AutentixSessionStatusResponse>
}
