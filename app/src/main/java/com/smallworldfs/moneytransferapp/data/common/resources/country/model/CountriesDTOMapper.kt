package com.smallworldfs.moneytransferapp.data.common.resources.country.model

import com.smallworldfs.moneytransferapp.data.countries.model.GetDestinationCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficeCountryResponse
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class CountriesDTOMapper @Inject constructor() {
    fun mapFromNetworkCountries(countryResponse: CountryResponse): CountriesDTO {
        val countriesDTO = CountriesDTO()
        countryResponse.data?.countries?.forEach {
            countriesDTO.countries.add(mapCountryResponseCountry(it))
        }
        return countriesDTO
    }

    private fun mapCountryResponseCountry(it: CountryResponse.Country) =
        CountryDTO(
            it.iso ?: "undefined",
            it.name ?: "undefined",
            it.logo ?: "undefined",
            phonePrefix = if (it.isoPhoneCode != null) "+${it.isoPhoneCode}" else "N/D",
            countryCode = it.isoPhoneCode.toString()
        )

    fun mapFromNetworkOriginCountries(response: GetOriginCountriesResponse, countryEntityList: CountryListEntity): CountriesDTO {
        val countriesDTO = CountriesDTO()
        val locatedCountry = response.data.located?.country
        response.data.countries?.forEach {
            val featured = it.iso?.equals(locatedCountry?.iso) ?: false
            countriesDTO.countries.add(
                CountryDTO(
                    it.iso ?: "undefined",
                    it.name ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.logo
                        ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.isoPhoneCode
                        ?: "N/D",
                    parseIsoPhoneCode(countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.isoPhoneCode),
                    featured
                )
            )
        }
        return countriesDTO
    }

    fun mapFromNetworkDestinationCountries(response: GetDestinationCountriesResponse, countryEntityList: CountryListEntity): CountriesDTO {
        val countriesDTO = CountriesDTO()
        response.data.countries?.forEach {
            countriesDTO.countries.add(
                CountryDTO(
                    it.iso ?: "undefined",
                    it.name ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.logo
                        ?: "undefined",
                    countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.isoPhoneCode
                        ?: "N/D",
                    parseIsoPhoneCode(countryEntityList.countries.find { entity -> entity.iso3 == it.iso }?.isoPhoneCode),
                    it.popular ?: false
                )
            )
        }
        return countriesDTO
    }

    fun mapFromLocalEntity(countryEntityList: CountryListEntity): CountriesDTO {
        val countriesDTO = CountriesDTO()
        countryEntityList.countries.forEach {
            countriesDTO.countries.add(
                CountryDTO(
                    it.iso3,
                    it.name,
                    it.logo,
                    it.isoPhoneCode,
                    parseIsoPhoneCode(it.isoPhoneCode),
                    it.featured
                )
            )
        }
        return countriesDTO
    }

    fun mapCountriesResponse(countriesResponse: CountriesResponse): CountriesDTO {
        val countriesDTO = CountriesDTO()
        countriesResponse.countries.forEach {
            countriesDTO.countries.add(CountryDTO(it.iso, it.name, it.isoPhoneCode, it.logo.url))
        }
        return countriesDTO
    }

    fun mapFromOfficeCountryResponse(officeCountryResponse: OfficeCountryResponse): CountriesDTO {
        val countriesDTO = CountriesDTO()
        officeCountryResponse.countries.forEach {
            countriesDTO.countries.add(CountryDTO(iso3 = it.keys.first(), name = it.values.first()))
        }
        return countriesDTO
    }

    private fun parseIsoPhoneCode(isoPhoneCode: String?) =
        isoPhoneCode?.removePrefix("+ ") ?: STRING_EMPTY
}
