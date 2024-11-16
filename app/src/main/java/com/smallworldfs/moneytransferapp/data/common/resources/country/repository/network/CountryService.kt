package com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network

import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetDestinationCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetStatesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryService {

    @GET("v4/{lang}/countries")
    fun getCountries(@Path("lang") lang: String): Call<CountryResponse>

    @GET("v4/{lang}/countries/origin")
    fun getOriginCountries(@Path("lang") lang: String, @Query("lat") latitude: Double? = null, @Query("lng") longitude: Double? = null): Call<GetOriginCountriesResponse>

    @GET("v4/{lang}/countries/{origin-country}/destinations")
    fun getDestinationCountries(@Path("lang") lang: String, @Path("origin-country") originCountry: String): Call<GetDestinationCountriesResponse>

    @GET("v4/{lang}/countries/{iso}/states")
    fun getStates(@Path("lang") lang: String, @Path("iso") iso: String): Call<GetStatesResponse>
}
