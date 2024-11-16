package com.smallworldfs.moneytransferapp.domain.migrated.mtn.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository.MTNRepository
import javax.inject.Inject

class MTNUseCase @Inject constructor(
    private val countryRepository: CountryRepository,
    private val mtnRepository: MTNRepository
) {

    fun getMtnStatus(mtn: String, country: String): OperationResult<MtnStatusDTO, Error> {
        return mtnRepository.getMtnStatus(mtn, country)
    }

    fun getCountries(): OperationResult<CountriesDTO, Error> {
        return countryRepository.getCountries()
    }
}
