package com.smallworldfs.moneytransferapp.data.common.resources.country.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CountryListEntity(
    val countries: MutableList<CountryEntity> = mutableListOf()
)
data class CountryEntity(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val logo: String = STRING_EMPTY,
    val isoPhoneCode: String = STRING_EMPTY,
    val featured: Boolean = false
)
