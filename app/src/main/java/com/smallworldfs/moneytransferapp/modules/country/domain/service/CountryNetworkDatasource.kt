package com.smallworldfs.moneytransferapp.modules.country.domain.service

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse
import com.smallworldfs.moneytransferapp.modules.country.domain.model.server.ServerCountryRequest
import retrofit2.Response
import rx.Observable

class CountryNetworkDatasource(
    private val service: CountryService
) : NetworkDatasource() {

    fun requestOriginCountries(request: ServerCountryRequest): Observable<Response<Country>> =
        executeCall(service.getCountries(request))

    fun requestPayoutCountries(request: ServerCountryRequest): Observable<Response<Country>> =
        executeCall(service.getCountries(request))

    fun requestPayoutCountriesNotSorted(request: ServerCountryRequest): Observable<Response<CountryResponse>> =
        executeCall(service.getCountriesObjects(request))

    fun requestCountryPrefix(request: ServerCountryRequest): Observable<Response<Country>> =
        executeCall(service.getCountries(request))
}
