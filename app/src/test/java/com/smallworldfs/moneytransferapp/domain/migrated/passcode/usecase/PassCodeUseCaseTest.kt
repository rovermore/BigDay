package com.smallworldfs.moneytransferapp.domain.migrated.passcode.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PassCodeUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    private lateinit var passCodeUseCase: PassCodeUseCase

    private val passCodeDTO = PassCodeDTO("passcode".toCharArray())

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        passCodeUseCase = PassCodeUseCase(
            userDataRepository,
        )
    }

    @Test
    fun `when calling savePasscode userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(false, false, false)
        val expectedResult = Success(true)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.activatePassCode()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.setPassCode(passCodeDTO)).thenReturn(expectedResult)

        val result = passCodeUseCase.savePasscode(passCodeDTO.code)

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(initialDTO, Mockito.times(1)).activatePassCode()
        Mockito.verify(userDataRepository, Mockito.times(1)).saveQuickLoginSettings(finalDTO)
        Mockito.verify(userDataRepository, Mockito.times(1)).setPassCode(passCodeDTO)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling savePasscode operation is forbidden, PassCodeUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        Mockito.`when`(initialDTO.activatePassCode()).thenReturn(expectedResult)

        val result = passCodeUseCase.savePasscode(passCodeDTO.code)

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling resetPasscode userDataRepository verifies operation and save changes`() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        val finalDTO = QuickLoginSettingsDTO(false, false, false)
        val expectedResult = Success(true)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(initialDTO.deactivatePassCode()).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.saveQuickLoginSettings(finalDTO)).thenReturn(Success(finalDTO))
        Mockito.`when`(userDataRepository.deletePassCode()).thenReturn(expectedResult)

        val result = passCodeUseCase.resetPasscode()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(initialDTO, Mockito.times(1)).deactivatePassCode()
        Mockito.verify(userDataRepository, Mockito.times(1)).saveQuickLoginSettings(finalDTO)
        Mockito.verify(userDataRepository, Mockito.times(1)).deletePassCode()
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling resetPasscode operation is forbidden, PassCodeUseCase returns error `() {
        val initialDTO = Mockito.mock(QuickLoginSettingsDTO::class.java)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        val expectedResult = Failure(Error.UnsupportedOperation("Can't deactivate passcode"))
        Mockito.`when`(initialDTO.deactivatePassCode()).thenReturn(expectedResult)

        val result = passCodeUseCase.resetPasscode()

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling validatePassCode userDataRepository verifies operation and returns validation OK`() {
        val initialDTO = QuickLoginSettingsDTO(false, false, true)
        val expectedResult = Success(true)
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(userDataRepository.getPassCode()).thenReturn(Success(passCodeDTO))

        val result = passCodeUseCase.validatePassCode(passCodeDTO)

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(userDataRepository, Mockito.times(1)).getPassCode()
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when calling validatePassCode userDataRepository verifies operation and returns validation KO`() {
        val initialDTO = QuickLoginSettingsDTO(false, false, true)
        val newPassCode = PassCodeDTO("new_passcode".toCharArray())
        val expectedFailure = Error.OperationCompletedWithError()::class
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))
        Mockito.`when`(userDataRepository.getPassCode()).thenReturn(Success(passCodeDTO))

        val result = passCodeUseCase.validatePassCode(newPassCode)

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verify(userDataRepository, Mockito.times(1)).getPassCode()
        assertEquals(Failure::class, result::class)
        assertEquals(expectedFailure, (result as Failure).reason::class)
    }

    @Test
    fun `when calling validatePassCode operation is forbidden, PassCodeUseCase returns error `() {
        val initialDTO = QuickLoginSettingsDTO(false, false, false)
        val expectedFailure = Error.UnsupportedOperation("Passcode is not active")::class
        Mockito.`when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(initialDTO))

        val result = passCodeUseCase.validatePassCode(passCodeDTO)

        Mockito.verify(userDataRepository, Mockito.times(1)).getQuickLoginSettings()
        Mockito.verifyNoMoreInteractions(userDataRepository)
        assertEquals(Failure::class, result::class)
        assertEquals(expectedFailure, (result as Failure).reason::class)
    }
}
