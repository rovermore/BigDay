package com.smallworldfs.moneytransferapp.domain.migrated.passwordconfirm.usecase

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.passwordconfirm.PasswordConfirmUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PasswordConfirmUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var base64Tool: Base64Tool

    lateinit var passwordConfirmUseCase: PasswordConfirmUseCase

    private val user = UserDTOMock.userDTO
    private val passwordString = "password"
    private val encodedPasswordString = "U21hbGxUZXN0MjMh"
    private val passwordDTO = PasswordDTO(encodedPasswordString.toCharArray())
    private val getUserSuccess = Success(user)
    private val getUserError = Failure(Error.UnregisteredUser("No user found"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        passwordConfirmUseCase = PasswordConfirmUseCase(
            userDataRepository,
            base64Tool
        )
    }

    @Test
    fun `when userDataRepository getUser success loginRepository checkPassword is called`() {
        `when`(base64Tool.encode(passwordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getLoggedUser()).thenReturn(getUserSuccess)
        `when`(
            userDataRepository.checkPassword(
                user.email,
                passwordDTO,
                user.country.countries[0].iso3
            )
        ).thenReturn(Success(true))
        passwordConfirmUseCase.checkPassword(passwordString.toCharArray())
        verify(userDataRepository, times(1)).checkPassword(
            user.email,
            passwordDTO,
            user.country.countries[0].iso3
        )
    }

    @Test
    fun `when userDataRepository getUser failure checkPassword returns failure`() {
        `when`(base64Tool.encode(passwordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getLoggedUser()).thenReturn(getUserError)
        val result = passwordConfirmUseCase.checkPassword(passwordString.toCharArray())
        val expectedResult = getUserError
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when loginRepository checkPassword success passwordConfirmUseCase checkPassword returns success`() {
        `when`(base64Tool.encode(passwordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getLoggedUser()).thenReturn(getUserSuccess)
        `when`(
            userDataRepository.checkPassword(
                user.email,
                passwordDTO,
                user.country.countries[0].iso3
            )
        ).thenReturn(Success(true))
        val result = passwordConfirmUseCase.checkPassword(passwordString.toCharArray())
        val expectedResult = Success(true)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when loginRepository checkPassword failure passwordConfirmUseCase checkPassword returns failure`() {
        `when`(base64Tool.encode(passwordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getLoggedUser()).thenReturn(getUserSuccess)
        `when`(
            userDataRepository.checkPassword(
                user.email,
                passwordDTO,
                user.country.countries[0].iso3
            )
        ).thenReturn(getUserError)
        val result = passwordConfirmUseCase.checkPassword(passwordString.toCharArray())
        val expectedResult = getUserError
        assertEquals(expectedResult, result)
    }
}
