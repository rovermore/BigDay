package com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryInfoResponse

interface CountryRepository {
    fun getCountries(): OperationResult<CountriesDTO, Error>
    fun getOriginCountries(latitude: Double, longitude: Double): OperationResult<CountriesDTO, Error>
    fun getOriginCountries(): OperationResult<CountriesDTO, Error>
    fun getCountryFromPhoneNumber(): OperationResult<String, Error>
    fun getDestinationCountries(origin: String): OperationResult<CountriesDTO, Error>
    fun setOriginCountry(country: CountryDTO): OperationResult<CountryDTO, Error>
    fun setDestinationCountry(country: CountryDTO): OperationResult<CountryDTO, Error>
    fun getStates(country: CountryDTO): OperationResult<List<StateDTO>, Error>
    fun requestCountryInfoLegacy(type: String, country: String): OperationResult<CountryInfoResponse, Error>
}
