package com.smallworldfs.moneytransferapp.data

import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryResponse

object CountryResponseMock {

    private val country1 = CountryResponse.Country(
        "es",
        "españa",
        34,
        "logoURL",

    )

    private val country2 = CountryResponse.Country(
        "uk",
        "Reino Unido",
        44,
        "logoURL",

    )

    private val data = CountryResponse.Data(
        mutableListOf(country1, country2)
    )

    val countryResponse = CountryResponse(data)
}
