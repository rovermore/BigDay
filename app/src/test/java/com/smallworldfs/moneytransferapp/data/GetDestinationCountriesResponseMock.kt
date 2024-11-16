package com.smallworldfs.moneytransferapp.data

import com.smallworldfs.moneytransferapp.data.countries.model.GetDestinationCountriesResponse

object GetDestinationCountriesResponseMock {

    private val country1Destination = GetDestinationCountriesResponse.Country(
        "es",
        "españa",
        true
    )

    private val country2Destionation = GetDestinationCountriesResponse.Country(
        "uk",
        "Reino Unido",
        false
    )

    private val dataDestination = GetDestinationCountriesResponse.Data(listOf(country1Destination, country2Destionation))

    val destinationResponse = GetDestinationCountriesResponse(dataDestination)
}
