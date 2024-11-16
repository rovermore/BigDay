package com.smallworldfs.moneytransferapp.presentation.common.countries

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import javax.inject.Inject

class CountryDataMapper @Inject constructor() {

    fun map(countriesDTO: CountriesDTO): CountriesData {
        val countriesData = CountriesData()
        countriesDTO.countries.forEach {
            countriesData.countries.add(CountryData(iso3 = it.iso3, name = it.name, url = it.logo))
        }
        return countriesData
    }
}
