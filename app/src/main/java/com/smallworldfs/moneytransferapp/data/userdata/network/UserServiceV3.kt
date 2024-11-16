package com.smallworldfs.moneytransferapp.data.userdata.network

import com.smallworldfs.moneytransferapp.data.marketing.model.FormSettingsServer
import com.smallworldfs.moneytransferapp.data.marketing.model.SaveMarketingPreferencesRequest
import com.smallworldfs.moneytransferapp.data.marketing.model.SaveMarketingPreferencesResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserServiceV3 {

    @GET(EndPoint.GDPR_MARKETIN_PREF)
    fun requestMarketingPreferences(
        @Query("userToken") userToken: String,
        @Query("userId") userid: String,
        @Query("view") fromView: String,
    ): Call<FormSettingsServer>

    @POST(EndPoint.GDPR_MARKETIN_PREF)
    fun saveMarketingPreferences(
        @QueryMap request: SaveMarketingPreferencesRequest
    ): Call<SaveMarketingPreferencesResponse>
}
