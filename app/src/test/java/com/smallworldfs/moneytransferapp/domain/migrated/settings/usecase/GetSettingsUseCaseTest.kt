package com.smallworldfs.moneytransferapp.domain.migrated.settings.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetSettingsUseCaseTest {

    private lateinit var getSettingsUseCase: GetSettingsUseCase

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var userTokenLocal: UserTokenLocal

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getSettingsUseCase = GetSettingsUseCase(
            settingsRepository,
            userDataRepository,
            userTokenLocal,
        )
    }

    @Test
    fun `settingsRepository is called when setNotificationsState is executed`() {
        getSettingsUseCase.setNotificationsState(true)
        verify(settingsRepository, times(1))
            .setNotificationsState(true)
    }

    @Test
    fun `user is cleaned when getSettings is executed with Unauthorized error`() {
        `when`(settingsRepository.getSettings())
            .thenReturn(Failure(Error.Unauthorized("Unauthorized error")))

        getSettingsUseCase.getSettings()

        verify(userDataRepository, times(1)).deletePassCode()
        verify(userDataRepository, times(1)).deletePassword()
        verify(userTokenLocal, times(1)).clearUserToken()
    }

    @Test
    fun `user is not cleaned when getSettings is executed with not Unauthorized error`() {
        `when`(settingsRepository.getSettings())
            .thenReturn(Failure(Error.Unmapped("Unmapped error")))

        getSettingsUseCase.getSettings()

        verify(userDataRepository, times(0)).deletePassCode()
        verify(userDataRepository, times(0)).deletePassword()
        verify(userTokenLocal, times(0)).clearUserToken()
    }

    @Test
    fun `when calling getQuickLoginSettings flow is OK, Success(QuickLoginSettingsDTO) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Success(initialDTO)

        val result = getSettingsUseCase.getQuickLoginSettings()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling getQuickLoginSettings flow is KO, Failure(OperationNotSupported) is returned`() {
        val expectedResult = Failure(Error.UnsupportedOperation())
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(expectedResult)

        val result = getSettingsUseCase.getQuickLoginSettings()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsEnabled userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, true, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.activateBiometrics()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))

        getSettingsUseCase.setBiometricsEnabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        verify(initialDTO, times(1)).activateBiometrics()
        verify(userDataRepository, times(1)).saveQuickLoginSettings(finalDTO)
    }

    @Test
    fun `when calling setBiometricsEnabled flow is OK, Success(QuickLoginSettingsDTO) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, true, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.activateBiometrics()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        val expectedResult = Success(finalDTO)

        val result = getSettingsUseCase.setBiometricsEnabled()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsEnabled operation is forbidden, QuickLoginUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        `when`(initialDTO.activateBiometrics()).thenReturn(expectedResult)

        val result = getSettingsUseCase.setBiometricsEnabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsDisabled userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.deactivateBiometrics()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))

        getSettingsUseCase.setBiometricsDisabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        verify(initialDTO, times(1)).deactivateBiometrics()
        verify(userDataRepository, times(1)).saveQuickLoginSettings(finalDTO)
    }

    @Test
    fun `when calling setBiometricsDisabled flow is OK, Success(finalDTO) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.deactivateBiometrics()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        val expectedResult = Success(finalDTO)

        val result = getSettingsUseCase.setBiometricsDisabled()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsDisabled operation is forbidden, QuickLoginUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't deactivate biometrics"))
        `when`(initialDTO.deactivateBiometrics()).thenReturn(expectedResult)

        val result = getSettingsUseCase.setBiometricsDisabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setPasscodeDisabled userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.deactivatePassCode()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))

        getSettingsUseCase.setPasscodeDisabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        verify(initialDTO, times(1)).deactivatePassCode()
        verify(userDataRepository, times(1)).saveQuickLoginSettings(finalDTO)
    }

    @Test
    fun `when calling setPasscodeDisabled flow is OK, Success(QuickLoginSettingsDTO) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        `when`(initialDTO.deactivatePassCode()).thenReturn(Success(finalDTO))
        `when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        val expectedResult = Success(finalDTO)

        val result = getSettingsUseCase.setPasscodeDisabled()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setPasscodeDisabled operation is forbidden, QuickLoginUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't deactivate passcode"))
        `when`(initialDTO.deactivatePassCode()).thenReturn(expectedResult)

        val result = getSettingsUseCase.setPasscodeDisabled()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        Assert.assertEquals(expectedResult, result)
    }
}
