package com.smallworldfs.moneytransferapp.domain.migrated.login.usecase

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.mocks.SWCoordinatesMock.coordinates
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class LoginUseCaseTest {

    @Mock
    lateinit var userTokenLocal: UserTokenLocal

    @Mock
    lateinit var base64Tool: Base64Tool

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var locationRepository: LocationRepository

    @Mock
    lateinit var countryRepository: CountryRepository

    lateinit var loginUseCase: LoginUseCase

    @Spy
    private val passwordDTO = PasswordDTO("SmallTest23!".toCharArray())

    private val user = UserDTOMock.userDTO
    private val email = "roberto@test.com"
    private val countryDTO = CountryDTO("ES")
    private val passWordString = "SmallTest23!"
    private val encodedPasswordString = "U21hbGxUZXN0MjMh"
    private val countryString = "ES"
    private val loginError = Failure(Error.UnregisteredUser("No user found in device"))
    private val loginSuccess = Success(user)
    private val uncompletedOperationError = Failure(Error.UncompletedOperation("Data couln't be readed"))
    private val locationNotAvailableError = Failure(Error.LocationUnavailable(STRING_EMPTY))

    private val countriesDTO = CountriesDTOMock.countriesDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        loginUseCase = LoginUseCase(
            userTokenLocal,
            base64Tool,
            userDataRepository,
            locationRepository,
            countryRepository,
        )
    }

    @Test
    fun `when login success loginRepository login is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)

        loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        verify(userDataRepository, times(1)).login(
            email,
            PasswordDTO(encodedPasswordString.toCharArray()),
            countryString,
        )
    }

    @Test
    fun `when login success loginRepository login returns success`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(loginSuccess)

        val result = loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        assertEquals(loginSuccess, result)
    }

    @Test
    fun `when login failure loginRepository login returns error`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(loginError)

        val result = loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        assertEquals(loginError, result)
    }

    @Test
    fun `when login success userTokenLocal setUserToken is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(Success(user))

        loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        verify(userTokenLocal, times(1)).setUserToken(user.userToken)
    }

    @Test
    fun `when login success PasswordDTO release is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(
            userDataRepository.login(
                email,
                passwordDTO,
                countryString,
            ),
        ).thenReturn(Success(user))

        loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        verify(passwordDTO, times(1)).release()
    }

    @Test
    fun `when login failure PasswordDTO release is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(
            userDataRepository.login(
                email,
                passwordDTO,
                countryString,
            ),
        ).thenReturn(loginError)

        loginUseCase.login(
            email,
            passwordDTO,
            countryDTO,
        )

        verify(passwordDTO, times(1)).release()
    }

    @Test
    fun `when autologin success userDataRepository getPassword is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))

        loginUseCase.autoLogin()

        verify(userDataRepository, times(1)).getPassword()
    }

    @Test
    fun `when autologin failure userDataRepository getPassword returns error`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(loginError)

        val result = loginUseCase.autoLogin()

        assertTrue(result is Failure)
    }

    @Test
    fun `when autologin success userDataRepository getUser is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))

        loginUseCase.autoLogin()

        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when autologin success loginRepository login is called`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(loginSuccess)

        loginUseCase.autoLogin()

        verify(userDataRepository, times(1)).login(
            email,
            PasswordDTO(encodedPasswordString.toCharArray()),
            countryString,
        )
    }

    @Test
    fun `when autologin success loginRepository login returns success`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(loginSuccess)

        val result = loginUseCase.autoLogin()

        assertEquals(loginSuccess, result)
    }

    @Test
    fun `when autologin failure loginRepository login returns failure`() {
        `when`(base64Tool.encode(passWordString)).thenReturn(encodedPasswordString)
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        `when`(
            userDataRepository.login(
                email,
                PasswordDTO(encodedPasswordString.toCharArray()),
                countryString,
            ),
        ).thenReturn(loginError)

        val result = loginUseCase.autoLogin()

        assertEquals(loginError, result)
    }

    @Test
    fun `when userDataRepository getUser failure autologin returns failure`() {
        `when`(userDataRepository.getPassword()).thenReturn(Success(passwordDTO))
        `when`(userDataRepository.getLoggedUser()).thenReturn(loginError)
        val result = loginUseCase.autoLogin()
        val expectedResult = loginError.reason.message
        assertEquals(expectedResult, (result.get() as Error.UnregisteredUser).message)
    }

    @Test
    fun `when checkPasscodeEnabled success userDataRepository getPasscode is called`() {
        val settingsDTO = QuickLoginSettingsDTO(false, false, true)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(settingsDTO))

        loginUseCase.isQuickLoginActive()

        verify(userDataRepository, times(1)).getQuickLoginSettings()
    }

    @Test
    fun `when pass code is enabled getPasscode returns success`() {
        val settingsDTO = QuickLoginSettingsDTO(false, false, true)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(settingsDTO))
        val expectedResult = Success(true)

        val result = loginUseCase.isQuickLoginActive()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when pass code is disabled getPasscode returns success`() {
        val settingsDTO = QuickLoginSettingsDTO(false, false, false)
        `when`(userDataRepository.getQuickLoginSettings()).thenReturn(Success(settingsDTO))
        val expectedResult = Success(false)

        val result = loginUseCase.isQuickLoginActive()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `when getExistingUser success userDataRepository getUser is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))

        loginUseCase.getExistingUser()

        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when getExistingUser success userDataRepository getUser return success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))

        val result = loginUseCase.getExistingUser()

        assertTrue(result is Success)
    }

    @Test
    fun `when getExistingUser failure userDataRepository getUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(uncompletedOperationError)

        val result = loginUseCase.getExistingUser()

        assertEquals(uncompletedOperationError, result)
    }

    @Test
    fun `when getExistingUser failure userDataRepository getUser returns limited user`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user.copy(status = UserDTO.LIMITED)))

        val result = loginUseCase.getExistingUser()

        assertTrue(result is Failure)
    }

    @Test
    fun `when getCountries success locationRepository getUserLocation returns success`() {
        `when`(locationRepository.getUserLocation()).thenReturn(Success(coordinates))
        `when`(countryRepository.getOriginCountries(coordinates.latitude, coordinates.longitude))
            .thenReturn(Success(countriesDTO))
        val result = loginUseCase.getCountries()
        assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when locationRepository getUserLocation returns error, getCountries is called without location`() {
        `when`(locationRepository.getUserLocation()).thenReturn(locationNotAvailableError)
        `when`(countryRepository.getOriginCountries())
            .thenReturn(Success(countriesDTO))
        val result = loginUseCase.getCountries()

        verify(countryRepository, times(1)).getOriginCountries()
        assertEquals(Success(countriesDTO), result)
    }
}
