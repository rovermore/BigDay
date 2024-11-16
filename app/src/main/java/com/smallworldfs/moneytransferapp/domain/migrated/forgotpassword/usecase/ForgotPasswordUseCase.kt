package com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.repository.ForgotPasswordRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val forgotPasswordRepository: ForgotPasswordRepository,
    private val countryRepository: CountryRepository
) {

    fun requestForgotPassword(email: String, country: String): OperationResult<Boolean, Error> =
        forgotPasswordRepository.requestForgotPassword(email, country)

    fun getCountries(): OperationResult<CountriesDTO, Error> =
        countryRepository.getOriginCountries()
}
