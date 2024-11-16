package com.smallworldfs.moneytransferapp.data.settings.network

import com.smallworldfs.moneytransferapp.data.settings.model.AppConfigResponse
import com.smallworldfs.moneytransferapp.data.settings.model.SettingsRequest
import com.smallworldfs.moneytransferapp.data.settings.model.SettingsResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.settings.domain.model.SettingsServer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SettingsService {

    @GET(EndPoint.SETTINGS)
    fun requestSettings(@QueryMap request: SettingsRequest): Call<SettingsResponse>

    @GET(EndPoint.CACHE_CONFIG)
    fun requestAppConfig(): Call<AppConfigResponse>

    @POST(EndPoint.GDPR_MARKETIN_PREF)
    fun saveMarketingPreferences(
        @Query("country") country: String,
        @Query("userId") userId: String,
        @Query("userToken") userToken: String,
        @Query("accept") accept: String,
        @Query("type") type: String
    ): Call<SettingsServer>
}
