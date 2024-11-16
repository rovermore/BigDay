package com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetDestinationCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.data.countries.model.GetStatesResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryInfoResponse
import javax.inject.Inject
import com.smallworldfs.moneytransferapp.modules.country.domain.service.CountryService as CountryServiceLegacy

class CountryNetworkDatasource @Inject constructor(
    private val service: CountryService,
    private val serviceLegacy: CountryServiceLegacy
) : NetworkDatasource() {

    fun requestInfo(lang: String): OperationResult<CountryResponse, APIError> =
        executeCall(service.getCountries(lang))

    fun requestCountriesOrigin(lang: String, latitude: Double, longitude: Double): OperationResult<GetOriginCountriesResponse, APIError> =
        executeCall(service.getOriginCountries(lang, latitude, longitude))

    fun requestCountriesOrigin(lang: String): OperationResult<GetOriginCountriesResponse, APIError> =
        executeCall(service.getOriginCountries(lang))

    fun requestDestinationCountries(lang: String, origin: String): OperationResult<GetDestinationCountriesResponse, APIError> =
        executeCall(service.getDestinationCountries(lang, origin))

    fun getStates(lang: String, country: String): OperationResult<GetStatesResponse, APIError> =
        executeCall(service.getStates(lang, country))

    fun requestCountryInfoLegacy(type: String, country: String): OperationResult<CountryInfoResponse, APIError> =
        executeCall(serviceLegacy.getCountryInfoLegacy(type, country))
}
