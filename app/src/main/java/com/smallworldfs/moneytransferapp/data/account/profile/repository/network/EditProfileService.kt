package com.smallworldfs.moneytransferapp.data.account.profile.repository.network

import com.smallworldfs.moneytransferapp.data.account.profile.model.CitiesOfStateResponse
import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormResponse
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface EditProfileService {

    @GET(EndPoint.EDIT_PROFILE_FORM)
    fun requestEditProfileFormAsync(@Query("userToken") userToken: String, @Query("userId") userId: String): Call<EditProfileFormResponse>

    @GET(EndPoint.GET_CITIES_FROM_COUNTRIES)
    fun requestCitiesFromState(@Query("country") country: String, @Query("state") state: String, @Query("type") type: String): Call<CitiesOfStateResponse>

    @PUT(EndPoint.EDIT_PROFILE)
    fun saveProfileForm(@QueryMap request: SaveProfileFormRequest): Call<SaveProfileFormResponse>
}
