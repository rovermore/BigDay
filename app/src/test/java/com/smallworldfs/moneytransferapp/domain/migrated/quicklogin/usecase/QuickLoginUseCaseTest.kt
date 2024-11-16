package com.smallworldfs.moneytransferapp.domain.migrated.quicklogin.usecase

import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.quicklogin.QuickLoginUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class QuickLoginUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    lateinit var quickLoginUseCase: QuickLoginUseCase

    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))
    private val user = UserDTOMock.userDTO
    private val successUser = Success(user)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        quickLoginUseCase = QuickLoginUseCase(
            userDataRepository,
        )
    }

    @Test
    fun `when calling setBiometricsEnabled userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, true, false)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.activateBiometrics()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))

        quickLoginUseCase.setBiometricsEnabled()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(initialDTO, Mockito.times(1)).activateBiometrics()
        Mockito.verify(userDataRepository, Mockito.times(1)).saveQuickLoginSettings(finalDTO)
    }

    @Test
    fun `when calling setBiometricsEnabled flow is OK, Success(true) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, true, false)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.activateBiometrics()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        val expectedResult = Success(true)

        val result = quickLoginUseCase.setBiometricsEnabled()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsEnabled operation is forbidden, QuickLoginUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        Mockito.`when`(initialDTO.activateBiometrics()).thenReturn(expectedResult)

        val result = quickLoginUseCase.setBiometricsEnabled()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsDisabled userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.deactivateBiometrics()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))

        quickLoginUseCase.setBiometricsDisabled()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(initialDTO, Mockito.times(1)).deactivateBiometrics()
        Mockito.verify(userDataRepository, Mockito.times(1)).saveQuickLoginSettings(finalDTO)
    }

    @Test
    fun `when calling setBiometricsDisabled flow is OK, Success(true) is returned`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(true, false, false)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.deactivateBiometrics()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        val expectedResult = Success(true)

        val result = quickLoginUseCase.setBiometricsDisabled()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling setBiometricsDisabled operation is forbidden, QuickLoginUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        Mockito.`when`(initialDTO.deactivateBiometrics()).thenReturn(expectedResult)

        val result = quickLoginUseCase.setBiometricsDisabled()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when getUserEmail success userDataRepository getUser is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        quickLoginUseCase.getUserEmail()
        Mockito.verify(userDataRepository, Mockito.times(1)).getLoggedUser()
    }

    @Test
    fun `when getUserEmail success userDataRepository getUser returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        val result = quickLoginUseCase.getUserEmail()
        Assert.assertEquals(Success(user.email), result)
    }

    @Test
    fun `when getUserEmail failure userDataRepository getUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = quickLoginUseCase.getUserEmail()
        Assert.assertEquals(error, result)
    }
}
