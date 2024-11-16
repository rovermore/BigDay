package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO

object CountriesDTOMock {

    val country1 = CountryDTO(
        "es",
        "españa",
        "logoURL",
        "+ 34",
        "34",
        false
    )

    val country2 = CountryDTO(
        "uk",
        "Reino Unido",
        "logoURL",
        "+ 44",
        "44",
        true
    )

    val countriesDTO = CountriesDTO(
        mutableListOf(country1, country2)
    )
}
