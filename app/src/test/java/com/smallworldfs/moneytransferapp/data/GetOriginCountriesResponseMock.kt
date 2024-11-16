package com.smallworldfs.moneytransferapp.data

import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse

object GetOriginCountriesResponseMock {

    private val country1Origin = GetOriginCountriesResponse.Country(
        "es",
        "españa"
    )

    private val country2Origin = GetOriginCountriesResponse.Country(
        "uk",
        "Reino Unido"
    )

    private val located = GetOriginCountriesResponse.Located(country1Origin)

    private val dataOrigin = GetOriginCountriesResponse.Data(located, mutableListOf(country1Origin, country2Origin))

    val originCountryResponse = GetOriginCountriesResponse(dataOrigin)
}
