package com.smallworldfs.moneytransferapp.data.settings

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.PreferencesLocal
import com.smallworldfs.moneytransferapp.data.settings.local.SettingsLocalDataSource
import com.smallworldfs.moneytransferapp.data.settings.network.SettingsNetworkDataSource
import com.smallworldfs.moneytransferapp.data.settings.repository.SettingsMapper
import com.smallworldfs.moneytransferapp.data.settings.repository.SettingsRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppConfigDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionExtractor
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import com.smallworldfs.moneytransferapp.mocks.dto.SettingsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.SettingsResponseMock
import com.smallworldfs.moneytransferapp.utils.Constants
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsRepositoryTest {

    @Mock
    lateinit var settingsNetworkDataSource: SettingsNetworkDataSource

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var settingsMapper: SettingsMapper

    @Mock
    lateinit var preferencesLocalDataSource: PreferencesLocal

    @Mock
    lateinit var versionExtractor: VersionExtractor

    @Mock
    lateinit var settingsLocalDatasource: SettingsLocalDataSource

    lateinit var settingsRepository: SettingsRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedDomainError = Error.Unmapped("Unmapped error")
    private val unregisteredUserDomainError = Error.UnregisteredUser("No user found in device")

    private val userDTOSuccess = Success(UserDTOMock.userDTO)

    private val settingsResponse = SettingsResponseMock.settingsResponse
    private val version = Version("4.12.1")
    private val settingsDTO = SettingsDTOMock.settingsDTO
    private val appConfigResponse = AppConfigResponseMock.appConfigResponse
    private val appConfigDTO = AppConfigDTO(Version(appConfigResponse.minVersion!!))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        settingsRepository = SettingsRepositoryImpl(
            settingsNetworkDataSource,
            settingsMapper,
            preferencesLocalDataSource,
            versionExtractor,
            userDataRepository,
            apiErrorMapper,
            settingsLocalDatasource
        )
    }

    @Test
    fun `when getSettings success settingsNetworkDataSource requestSettings is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(versionExtractor.getAppVersion()).thenReturn(version)
        Mockito.`when`(preferencesLocalDataSource.isNotificationsEnabled()).thenReturn(true)
        Mockito.`when`(
            settingsMapper.map(
                settingsResponse,
                versionExtractor.getAppVersion().name,
                preferencesLocalDataSource.isNotificationsEnabled(),
                userDTOSuccess.get().status == Constants.UserType.LIMITED,
                userDTOSuccess.get().country.countries.first().iso3,
                userDTOSuccess.get().email
            )
        ).thenReturn(settingsDTO)
        Mockito.`when`(
            settingsNetworkDataSource.requestSettings(
                userDTOSuccess.get().country.countries.first().iso3
            )
        ).thenReturn(Success(settingsResponse))
        settingsRepository.getSettings()
        Mockito.verify(settingsNetworkDataSource, Mockito.times(1))
            .requestSettings(userDTOSuccess.get().country.countries.first().iso3)
    }

    @Test
    fun `when getSettings success settingsNetworkDataSource requestSettings returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(versionExtractor.getAppVersion()).thenReturn(version)
        Mockito.`when`(preferencesLocalDataSource.isNotificationsEnabled()).thenReturn(true)
        Mockito.`when`(
            settingsMapper.map(
                settingsResponse,
                versionExtractor.getAppVersion().name,
                preferencesLocalDataSource.isNotificationsEnabled(),
                userDTOSuccess.get().status == Constants.UserType.LIMITED,
                userDTOSuccess.get().country.countries.first().iso3,
                userDTOSuccess.get().email
            )
        ).thenReturn(settingsDTO)
        Mockito.`when`(
            settingsNetworkDataSource.requestSettings(
                userDTOSuccess.get().country.countries.first().iso3
            )
        ).thenReturn(Success(settingsResponse))
        val result = settingsRepository.getSettings()
        Assert.assertEquals(Success(settingsDTO), result)
    }

    @Test
    fun `when getSettings failure settingsNetworkDataSource requestSettings returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(versionExtractor.getAppVersion()).thenReturn(version)
        Mockito.`when`(preferencesLocalDataSource.isNotificationsEnabled()).thenReturn(true)
        Mockito.`when`(
            settingsNetworkDataSource.requestSettings(
                userDTOSuccess.get().country.countries.first().iso3
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = settingsRepository.getSettings()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getSettings failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = settingsRepository.getSettings()
        Assert.assertEquals(
            Failure(unregisteredUserDomainError).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when setNotificationsState success preferencesLocalDataSource setNotificationsEnabled is called`() {
        settingsRepository.setNotificationsState(true)
        Mockito.verify(preferencesLocalDataSource, Mockito.times(1))
            .setNotificationsEnabled(true)
    }

    @Test
    fun `when setOnboardingShown success preferencesLocalDataSource onBoardShown is called`() {
        settingsRepository.setOnboardingShown(true)
        Mockito.verify(preferencesLocalDataSource, Mockito.times(1))
            .onBoardShown(true)
    }

    @Test
    fun `when isOnBoardShown success preferencesLocalDataSource isOnBoardShown is called`() {
        settingsRepository.isOnBoardShown()
        Mockito.verify(preferencesLocalDataSource, Mockito.times(1))
            .isOnBoardShown()
    }

    @Test
    fun `when isOnBoardShown success preferencesLocalDataSource isOnBoardShown returns success`() {
        Mockito.`when`(preferencesLocalDataSource.isOnBoardShown()).thenReturn(true)
        val result = settingsRepository.isOnBoardShown()
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when getAppConfig success settingsNetworkDataSource requestAppConfig is called`() {
        Mockito.`when`(settingsNetworkDataSource.requestAppConfig())
            .thenReturn(Success(appConfigResponse))
        settingsRepository.getAppConfig()
        Mockito.verify(settingsNetworkDataSource, Mockito.times(1))
            .requestAppConfig()
    }

    @Test
    fun `when getAppConfig success settingsNetworkDataSource requestAppConfig returns success`() {
        Mockito.`when`(settingsNetworkDataSource.requestAppConfig())
            .thenReturn(Success(appConfigResponse))
        val result = settingsRepository.getAppConfig()
        Assert.assertEquals(
            Success(appConfigDTO).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when getAppConfig failure settingsNetworkDataSource requestAppConfig returns error`() {
        Mockito.`when`(settingsNetworkDataSource.requestAppConfig())
            .thenReturn(Failure(unmappedApiError))
        val result = settingsRepository.getAppConfig()
        Assert.assertEquals(
            Error.OperationCompletedWithError().javaClass.simpleName,
            result.get().javaClass.simpleName
        )
    }
}
