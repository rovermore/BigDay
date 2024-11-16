package com.smallworldfs.moneytransferapp.presentation.freeuser

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import javax.inject.Inject

class CountryUIModelMapper @Inject constructor() {

    fun mapToUIModel(countryDTO: CountryDTO) = CountryUIModel(countryDTO.iso3, countryDTO.name, countryDTO.logo, countryDTO.phonePrefix, countryDTO.countryCode, countryDTO.featured)
    fun mapToUIModel(countries: List<CountryDTO>) = countries.map { mapToUIModel(it) }
    fun mapToDTO(countryUIModel: CountryUIModel) = CountryDTO(countryUIModel.iso3, countryUIModel.name, countryUIModel.logo, countryUIModel.prefix, countryUIModel.countryCode, countryUIModel.featured)
}
