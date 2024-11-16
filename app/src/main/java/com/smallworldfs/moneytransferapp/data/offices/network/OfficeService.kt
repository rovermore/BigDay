package com.smallworldfs.moneytransferapp.data.offices.network

import com.smallworldfs.moneytransferapp.data.offices.model.CityResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficeCountryResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesPoiResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OfficeService {

    @GET(EndPoint.COUNTRY)
    fun requestCountries(@Query("type") type: String, @Query("countryOrigin") countryOrigin: String): Call<OfficeCountryResponse>

    @GET(EndPoint.LOCATION_CITIES)
    fun requestCities(@Query("country") country: String): Call<CityResponse>

    @GET(EndPoint.SEARCH_OFFICES)
    fun requestOffices(@Query("country") country: String, @Query("city") city: String): Call<OfficesResponse>

    @GET(EndPoint.SEARCH_OFFICES_POIS)
    fun requestPoi(@Query("longitude") longitude: String, @Query("latitude") latitude: String): Call<OfficesPoiResponse>
}
