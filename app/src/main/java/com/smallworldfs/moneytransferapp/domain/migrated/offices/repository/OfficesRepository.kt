package com.smallworldfs.moneytransferapp.domain.migrated.offices.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO

interface OfficesRepository {
    fun requestCountries(): OperationResult<CountriesDTO, Error>
    fun requestCities(country: String): OperationResult<List<CityDTO>, Error>
    fun requestOffices(country: String, city: String): OperationResult<List<OfficeDTO>, Error>
    fun requestOfficesPoi(longitude: String, latitude: String): OperationResult<List<OfficeDTO>, Error>
}
