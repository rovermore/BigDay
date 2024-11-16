package com.smallworldfs.moneytransferapp.domain.migrated.splash.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionChecker
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionExtractor
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val countryRepository: CountryRepository,
    private val oAuthRepository: OAuthRepository,
    private val settingsRepository: SettingsRepository,
    private val userDataRepository: UserDataRepository,
    private val versionExtractor: VersionExtractor,
    private val versionChecker: VersionChecker,
    private val operationsRepository: OperationsRepository
) {

    companion object {
        private const val OAUTH = "oauth"
    }

    fun checkUserStatus(): OperationResult<UserDTO, Error> {
        return userDataRepository.getLoggedUser()
            .mapFailure {
                return Failure(Error.UnregisteredUser("Unregistered user"))
            }
    }

    fun loadInitialCountryData() {
        countryRepository.getCountries()
        countryRepository.getOriginCountries()
    }

    fun loadAppConfig(): OperationResult<String, Error> {
        return operationsRepository.getIntegrityDTO(OAUTH)
            .map {
                val appTokenResult = oAuthRepository.refreshOAuthTokenAsync(it)
                val appToken = if (appTokenResult is Success) appTokenResult.get() else ""
                return settingsRepository.getAppConfig()
                    .map { appConfigDTO ->
                        val serverVersion = appConfigDTO.minVersion
                        val appVersion = versionExtractor.getAppVersion()
                        return versionChecker.checkAppVersion(appVersion, serverVersion)
                            .map { result ->
                                if (result == VersionChecker.OperationCheck.FORCE) {
                                    return Failure(Error.UpdateRequired())
                                } else {
                                    return Success(appToken)
                                }
                            }.mapFailure {
                                return Success(appToken)
                            }
                    }
            }.peekFailure {
                return Failure(it)
            }
    }

    fun isOnBoardShown() = settingsRepository.isOnBoardShown()
}
