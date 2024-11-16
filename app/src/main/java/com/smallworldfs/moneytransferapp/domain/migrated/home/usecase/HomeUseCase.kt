package com.smallworldfs.moneytransferapp.domain.migrated.home.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerDeliveryMethodsRequest
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.settings.domain.model.SettingsServer
import com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository as CountryRepositoryLegacy

class HomeUseCase @Inject constructor(
    private val countryRepository: CountryRepository,
    private val userDataRepository: UserDataRepository,
    private val settingsRepository: SettingsRepository,
) {

    fun getUser(): OperationResult<UserDTO, Error> =
        userDataRepository.getLoggedUser()

    fun getDeliveryMethodsLegacy() {
        // TODO: Remove this when migrate EditBeneficiaryInteractor, CheckoutInteractor and TransactionalInteractor
        CalculatorRepositoryLegacy.getInstance().requestDeliveryMethods(ServerDeliveryMethodsRequest())
    }

    fun getUserCountryInfoLegacy() {
        // TODO: Remove this when migrate SendToInteractor and PromotionsCodeInteractor
        userDataRepository.getLoggedUser()
            .map {
                countryRepository.requestCountryInfoLegacy(
                    COUNTRY.ORIGIN_COUNTRIES_TYPE,
                    it.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY,
                ).map { response ->
                    CountryRepositoryLegacy.getInstance().setCurrentUserCountryUserInfo(response.country)
                }
            }
    }

    fun isLimitedUser(): OperationResult<Boolean, Error> =
        userDataRepository.getLoggedUser()
            .map {
                it.isLimited()
            }

    fun getGDPRInfo(): OperationResult<Gdpr, Error> =
        userDataRepository.getLoggedUser()
            .map {
                it.gdpr
            }

    fun setSMSConsent() {
        userDataRepository.setSMSConsentShown()
    }

    fun getLastPrimeForPushEventTimestamp(): Long =
        userDataRepository.getLastPrimeForPushEventTimestamp()

    fun setLastPrimeForPushEventTimestamp(timestamp: Long) {
        userDataRepository.setLastPrimeForPushEventTimestamp(timestamp)
    }

    fun getUserData(): OperationResult<UserDTO, Error> =
        userDataRepository.getUserData()

    fun setUserData(user: User) {
        userDataRepository.putUser(user)
    }

    fun sendGDPRResponse(accept: Boolean): OperationResult<SettingsServer, Error> =
        userDataRepository.getLoggedUser()
            .map {
                return settingsRepository.saveSettings(
                    it.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY,
                    it.id,
                    it.userToken,
                    if (accept) "1" else "0",
                ).map { settingsResponse ->
                    settingsResponse
                }.mapFailure { error ->
                    error
                }
            }.mapFailure {
                it
            }
}
