package com.smallworldfs.moneytransferapp.data.common.resources.country.model

data class CountriesResponse(
    val countries: List<NewCountryEntity>
)

data class NewCountryEntity(
    val iso: String,
    val name: String,
    val isoPhoneCode: String,
    val logo: Logo
)

data class Logo(
    val url: String
)
