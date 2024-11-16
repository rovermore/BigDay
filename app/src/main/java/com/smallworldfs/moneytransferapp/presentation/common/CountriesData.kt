package com.smallworldfs.moneytransferapp.presentation.common

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class CountriesData(
    val countries: MutableList<CountryData> = mutableListOf()
)

data class CountryData(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY
)
