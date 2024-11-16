package com.smallworldfs.moneytransferapp.data.common.resources.country.model

data class CountryResponse(val data: Data?) {
    data class Data(val countries: List<Country>)
    data class Country(val iso: String?, val name: String?, val isoPhoneCode: Int?, val logo: String?)
}
