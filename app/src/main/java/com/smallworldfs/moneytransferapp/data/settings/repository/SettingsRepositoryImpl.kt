package com.smallworldfs.moneytransferapp.data.settings.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.PreferencesLocal
import com.smallworldfs.moneytransferapp.data.settings.local.SettingsLocalDataSource
import com.smallworldfs.moneytransferapp.data.settings.network.SettingsNetworkDataSource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppConfigDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.SettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionExtractor
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import com.smallworldfs.moneytransferapp.modules.settings.domain.model.SettingsServer
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsNetworkDataSource: SettingsNetworkDataSource,
    private val settingsMapper: SettingsMapper,
    private val preferencesLocalDataSource: PreferencesLocal,
    private val versionExtractor: VersionExtractor,
    private val userDataRepository: UserDataRepository,
    private val apiErrorMapper: APIErrorMapper,
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override fun getSettings(): OperationResult<SettingsDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map { user ->
                val country = user.country.countries.first().iso3
                return settingsNetworkDataSource.requestSettings(country)
                    .mapFailure {
                        apiErrorMapper.map(it)
                    }.map { response ->
                        settingsMapper.map(
                            response,
                            versionExtractor.getAppVersion().name,
                            preferencesLocalDataSource.isNotificationsEnabled(),
                            user.status == Constants.UserType.LIMITED,
                            country,
                            user.email,
                        )
                    }
            }
            .mapFailure {
                return Failure<Error>(Error.UnregisteredUser("No user found in device"))
            }
    }

    override fun setNotificationsState(enable: Boolean) {
        preferencesLocalDataSource.setNotificationsEnabled(enable)
    }

    override fun getAppConfig(): OperationResult<AppConfigDTO, Error> {
        return settingsNetworkDataSource.requestAppConfig()
            .map {
                settingsLocalDataSource.saveBrazeStatus(it.brazeEnabled)
                AppConfigDTO(Version(it.minVersion ?: "0.0.1"))
            }.mapFailure {
                Error.OperationCompletedWithError()
            }
    }

    override fun setOnboardingShown(shown: Boolean) {
        preferencesLocalDataSource.onBoardShown(shown)
    }

    override fun isOnBoardShown(): OperationResult<Boolean, Error> {
        return Success(preferencesLocalDataSource.isOnBoardShown())
    }

    override fun saveSettings(country: String, userId: String, userToken: String, accept: String): OperationResult<SettingsServer, Error> =
        settingsNetworkDataSource.saveSettings(country, userId, userToken, accept)
            .mapFailure {
                apiErrorMapper.map(it)
            }
}
