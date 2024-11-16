package com.smallworldfs.moneytransferapp.data.common.resources.country.model

import com.smallworldfs.moneytransferapp.data.countries.model.GetDestinationCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetStatesResponse
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class StatesDTOMapper @Inject constructor() {
    fun mapFromNetworkStates(response: GetStatesResponse): List<StateDTO> {
        val result = mutableListOf<StateDTO>()
        response.data.states?.let { states ->
            states.forEach {
                result.add(StateDTO(it.code ?: "N/D", it.name ?: "undefined", it.logo?.url ?: "undefined", it.active ?: false))
            }
        }
        return result
    }

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
                    STRING_EMPTY,
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
                    STRING_EMPTY,
                    it.popular ?: false
                )
            )
        }
        return countriesDTO
    }

    fun mapFromLocalEntity(countryEntityList: CountryListEntity): CountriesDTO {
        val countriesDTO = CountriesDTO()
        countryEntityList.countries.forEach {
            countriesDTO.countries.add(CountryDTO(it.iso3, it.name, it.logo, it.isoPhoneCode))
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
}
