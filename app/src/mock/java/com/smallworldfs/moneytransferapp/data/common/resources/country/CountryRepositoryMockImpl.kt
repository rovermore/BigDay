package com.smallworldfs.moneytransferapp.data.common.resources.country

import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryResponse
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import io.reactivex.Observable
import javax.inject.Inject

class CountryRepositoryMockImpl @Inject constructor() : CountryRepository {
    override suspend fun requestCountriesPrefix(): CountryResponse? {
        return CountryResponse()
    }

    override suspend fun requestCountriesOrigin(): CountryResponse? {
        return CountryResponse()
    }

    override fun requestOriginCountries(): Observable<CountryResponse> {
        return Observable.just(CountryResponse())
    }
}