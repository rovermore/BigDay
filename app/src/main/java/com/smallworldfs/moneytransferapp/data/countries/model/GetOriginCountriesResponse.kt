package com.smallworldfs.moneytransferapp.data.countries.model

data class GetOriginCountriesResponse(val data: Data) {
    data class Data(val located: Located?, val countries: List<Country>?)
    data class Located(val country: Country?)
    data class Country(val iso: String?, val name: String?)
}
