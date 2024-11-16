package com.smallworldfs.moneytransferapp.domain.migrated.splash.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.AppConfigDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionChecker
import com.smallworldfs.moneytransferapp.domain.migrated.version.VersionExtractor
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import com.smallworldfs.moneytransferapp.mocks.dto.IntegrityDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SplashUseCaseTest {

    @Mock
    private lateinit var countryRepository: CountryRepository

    @Mock
    private lateinit var oAuthRepository: OAuthRepository

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var operationsRepository: OperationsRepository

    @Mock
    private lateinit var versionExtractor: VersionExtractor

    @Mock
    private lateinit var versionChecker: VersionChecker

    private lateinit var splashUseCase: SplashUseCase

    private val unregisteredUserErrorMock = Failure(Error.UnregisteredUser("Unregistered user"))
    private val userMock = UserDTOMock.userDTO
    private val successUserMock = Success(userMock)
    private val successAppTokenMock = Success("AppTokenMock")
    private val currentAppVersionMock = Version("1.0.0")
    private val validAppVersionMock = Version("1.0.0")
    private val upperAppVersionMock = Version("2.0.0")
    private val invalidVersion = Version("----")
    private val errorUpdateRequired = Failure(Error.UpdateRequired())
    private val forceUpdateVersion = AppConfigDTO(upperAppVersionMock)
    private val validAppConfigDTOMock = AppConfigDTO(validAppVersionMock)
    private val invalidAppConfigDTOMock = AppConfigDTO(invalidVersion)
    private val integrityDTOMock = IntegrityDTOMock.integrityDTO
    private val successIntegrity = Success(integrityDTOMock)
    private val oauthOperation = "oauth"
    private val error = Failure(Error.OperationCompletedWithError())

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        splashUseCase = SplashUseCase(
            countryRepository,
            oAuthRepository,
            settingsRepository,
            userDataRepository,
            versionExtractor,
            versionChecker,
            operationsRepository
        )
    }

    @Test
    fun `when calling checkUserStatus, userDataRepository getUser is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUserMock)
        splashUseCase.checkUserStatus()
        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when calling getUserStatus, if user exists Success is returned`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUserMock)
        val result = splashUseCase.checkUserStatus()
        assertEquals(successUserMock, result)
    }

    @Test
    fun `when calling getUserStatus, if user not exists UnregisteredUser error is returned`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(unregisteredUserErrorMock)
        val result = splashUseCase.checkUserStatus()
        assertEquals(unregisteredUserErrorMock.javaClass, result.javaClass)
    }

    @Test
    fun `when calling loadInitialCountryData, countryRepository getCountries is called`() {
        splashUseCase.loadInitialCountryData()
        verify(countryRepository, times(1)).getCountries()
    }

    @Test
    fun `when calling loadInitialCountryData, countryRepository getOriginCountries is called`() {
        splashUseCase.loadInitialCountryData()
        verify(countryRepository, times(1)).getOriginCountries()
    }

    @Test
    fun `when calling loadAppConfig, operationsRepository getIntegrityDTO is called`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        splashUseCase.loadAppConfig()
        verify(oAuthRepository, times(1)).refreshOAuthTokenAsync(integrityDTOMock)
    }

    @Test
    fun `when calling loadAppConfig, oAuthRepository refreshOAuthTokenAsync is called`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        splashUseCase.loadAppConfig()
        verify(oAuthRepository, times(1)).refreshOAuthTokenAsync(integrityDTOMock)
    }

    @Test
    fun `when calling loadAppConfig, settingsRepository getAppConfig is called`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        splashUseCase.loadAppConfig()
        verify(settingsRepository, times(1)).getAppConfig()
    }

    @Test
    fun `when calling loadAppConfig, versionExtractor getAppVersion is called`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        splashUseCase.loadAppConfig()
        verify(versionExtractor, times(1)).getAppVersion()
    }

    @Test
    fun `when calling loadAppConfig, versionChecker checkAppVersion is called`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        splashUseCase.loadAppConfig()
        verify(versionChecker, times(1)).checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)
    }

    @Test
    fun `when calling loadAppConfig, if app version is up to date Success is returned`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        val result = splashUseCase.loadAppConfig()
        assertEquals(successAppTokenMock, result)
    }

    @Test
    fun `when calling loadAppConfig, if app version is not up to date UpdateRequired error is returned`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(forceUpdateVersion))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, forceUpdateVersion.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.FORCE))
        val result = splashUseCase.loadAppConfig()
        assertEquals(errorUpdateRequired.javaClass, result.javaClass)
    }

    @Test
    fun `when calling loadAppConfig, if an error occurs requesting config OperationCompletedWithError error is returned`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(successIntegrity)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(invalidAppConfigDTOMock))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, invalidAppConfigDTOMock.minVersion)).thenReturn(Failure(Error.UncompletedOperation()))
        val result = splashUseCase.loadAppConfig()
        assertEquals(successAppTokenMock, result)
    }

    @Test
    fun `when calling loadAppConfig, if an error occurs requesting integrity OperationCompletedWithError error is returned`() {
        `when`(operationsRepository.getIntegrityDTO(oauthOperation)).thenReturn(error)
        `when`(oAuthRepository.refreshOAuthTokenAsync(integrityDTOMock)).thenReturn(successAppTokenMock)
        `when`(settingsRepository.getAppConfig()).thenReturn(Success(validAppConfigDTOMock))
        `when`(versionChecker.checkAppVersion(currentAppVersionMock, validAppConfigDTOMock.minVersion)).thenReturn(Success(VersionChecker.OperationCheck.NOTHING))
        `when`(versionExtractor.getAppVersion()).thenReturn(currentAppVersionMock)
        val result = splashUseCase.loadAppConfig()
        assertEquals(error, result)
    }

    @Test
    fun `when isOnBoardShown is called settingsRepository isOnBoardShown is called`() {
        `when`(settingsRepository.isOnBoardShown()).thenReturn(Success(true))
        splashUseCase.isOnBoardShown()
        verify(settingsRepository, times(1)).isOnBoardShown()
    }

    @Test
    fun `when isOnBoardShown return true settingsRepository isOnBoardShown return true`() {
        `when`(settingsRepository.isOnBoardShown()).thenReturn(Success(true))
        val result = splashUseCase.isOnBoardShown()
        assertEquals(Success(true), result)
    }

    @Test
    fun `when isOnBoardShown return false settingsRepository isOnBoardShown return false`() {
        `when`(settingsRepository.isOnBoardShown()).thenReturn(Success(false))
        val result = splashUseCase.isOnBoardShown()
        assertEquals(Success(false), result)
    }
}
