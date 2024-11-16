package com.smallworldfs.moneytransferapp.data.common.resources.country.model

import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import javax.inject.Inject

class CountriesEntityMapper @Inject constructor() {
    fun mapFromNetworkCountries(countries: List<CountryResponse.Country>): CountryListEntity {
        val countriesEntity = CountryListEntity()
        countries.forEach {
            countriesEntity.countries.add(
                CountryEntity(
                    it.iso ?: "undefined",
                    it.name ?: "undefined",
                    it.logo ?: "undefined",
                    isoPhoneCode = if (it.isoPhoneCode != null) "+ ${it.isoPhoneCode}" else "N/D"
                )
            )
        }
        return countriesEntity
    }

    fun mapFromNetworkOriginCountries(
        originCountries: List<GetOriginCountriesResponse.Country>,
        countryEntityList: CountryListEntity,
        locatedCountry: GetOriginCountriesResponse.Located??
    ): CountryListEntity {
        val countriesEntity = CountryListEntity()
        originCountries.forEach {
            val featured = it.iso?.equals(locatedCountry?.country?.iso) ?: false
            countriesEntity.countries.add(
                CountryEntity(
                    it.iso ?: "undefined",
                    it.name ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.logo
                        ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.isoPhoneCode
                        ?: "N/D",
                    featured
                )
            )
        }
        return countriesEntity
    }

    fun mapFromLocalEntity(countryEntityList: CountryListEntity): CountriesDTO {
        val countriesDTO = CountriesDTO()
        countryEntityList.countries.forEach {
            countriesDTO.countries.add(CountryDTO(it.iso3, it.name, it.logo, it.isoPhoneCode))
        }
        return countriesDTO
    }
}
