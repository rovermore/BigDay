package com.smallworldfs.moneytransferapp.domain.migrated.searchcountry

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class SearchCountryUseCase @Inject constructor(
    private val countryRepository: CountryRepository,
    private val userDataRepository: UserDataRepository
) {

    fun getDestinationCountries(): OperationResult<CountriesDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return countryRepository.getDestinationCountries(it.country.countries.first().iso3)
            }
    }
}
