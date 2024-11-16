package com.smallworldfs.moneytransferapp.data.offices.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.offices.model.CityResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficeCountryResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesPoiResponse
import com.smallworldfs.moneytransferapp.data.offices.model.OfficesResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

class OfficeNetworkDatasource(private val service: OfficeService) : NetworkDatasource() {

    fun requestCountries(type: String, countryOrigin: String): OperationResult<OfficeCountryResponse, APIError> =
        executeCall(service.requestCountries(type = type, countryOrigin = countryOrigin,))

    fun requestCities(country: String): OperationResult<CityResponse, APIError> =
        executeCall(service.requestCities(country = country,))

    fun requestOffices(country: String, city: String): OperationResult<OfficesResponse, APIError> =
        executeCall(service.requestOffices(country = country, city = city,))

    fun requestPoi(longitude: String, latitude: String): OperationResult<OfficesPoiResponse, APIError> =
        executeCall(service.requestPoi(longitude, latitude))
}
