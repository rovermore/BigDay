package com.smallworldfs.moneytransferapp.data.countries.model

data class GetDestinationCountriesResponse(val data: Data) {
    data class Data(val countries: List<Country>?)
    data class Country(val iso: String?, val name: String?, val popular: Boolean?)
}
