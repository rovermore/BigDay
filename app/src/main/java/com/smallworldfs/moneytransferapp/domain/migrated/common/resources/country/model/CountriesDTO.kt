package com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CountriesDTO(
    val countries: MutableList<CountryDTO> = mutableListOf()
)

data class CountryDTO(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val logo: String = STRING_EMPTY,
    val phonePrefix: String = STRING_EMPTY,
    val countryCode: String = STRING_EMPTY,
    val featured: Boolean = false,
)
