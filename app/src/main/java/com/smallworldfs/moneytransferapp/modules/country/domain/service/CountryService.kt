package com.smallworldfs.moneytransferapp.modules.country.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryInfoResponse
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse
import com.smallworldfs.moneytransferapp.modules.country.domain.model.server.ServerCountryRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

interface CountryService {

    @GET(EndPoint.COUNTRY)
    fun getCountries(@QueryMap request: ServerCountryRequest?): Observable<Response<Country>?>?

    @GET(EndPoint.COUNTRY)
    fun getCountriesObjects(@QueryMap request: ServerCountryRequest?): Observable<Response<CountryResponse>?>?

    @GET(EndPoint.COUNTRY_INFO)
    fun getCountryInfoLegacy(
        @Query("type") type: String,
        @Query("country") country: String,
    ): Call<CountryInfoResponse>
}
