package com.smallworldfs.moneytransferapp.domain.migrated.freeuser.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.freeuser.CountrySelectionUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.model.Coordinates
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TransferCountrySelectionUseCaseTest {

    @Mock
    lateinit var countryRepository: CountryRepository

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var settingsRepository: SettingsRepository

    @Mock
    lateinit var userTokenLocal: UserTokenLocal

    @Mock
    lateinit var capabilityChecker: CapabilityChecker

    @Mock
    lateinit var locationRepository: LocationRepository

    private lateinit var countrySelectionUseCase: CountrySelectionUseCase

    private val validLatitude = 0.0
    private val validLongitude = 0.0
    private val validCoordinates = Coordinates.LatLon(validLatitude, validLongitude)
    private val invalidCoordinates = Coordinates.InvalidCoordinates
    private val originIso = "ESP"
    private val originCountry = CountryDTO(
        iso3 = "ES",
        name = "España",
        phonePrefix = "+34",
        countryCode = "34",
    )
    private val destinationCountry = CountryDTO(
        iso3 = "ES",
        name = "España",
        phonePrefix = "+34",
        countryCode = "34",
    )
    private val user = UserDTOMock.userDTO
    private val limitedLoginSuccess = Success(user)
    private val limitedLoginFailure = Failure(Error.UncompletedOperation("Operation not completed"))
    private val limitedUser = user.copy(destinationCountry = originCountry, country = user.country)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        countrySelectionUseCase = CountrySelectionUseCase(
            countryRepository,
            userDataRepository,
            settingsRepository,
            userTokenLocal,
            capabilityChecker,
            locationRepository,
        )
    }

    @Test
    fun `when origin countries is requested with coordinates countryRepository getOriginCountries is called`() {
        countrySelectionUseCase.getOriginCountries(validCoordinates)
        verify(countryRepository, times(1)).getOriginCountries(
            validLatitude, validLongitude,
        )
    }

    @Test
    fun `when origin countries is requested with invalid coordinates countryRepository getOriginCountries is called`() {
        countrySelectionUseCase.getOriginCountries(invalidCoordinates)
        verify(countryRepository, times(1)).getOriginCountries()
    }

    @Test
    fun `when destination countries is requested countryRepository getDestinationCountries is called`() {
        countrySelectionUseCase.getDestinationCountries(originIso)
        verify(countryRepository, times(1)).getDestinationCountries(originIso)
    }

    @Test
    fun `when origin country is set countryRepository setOriginCountry is called`() {
        countrySelectionUseCase.setOriginCountry(originCountry)
        verify(countryRepository, times(1)).setOriginCountry(originCountry)
    }

    @Test
    fun `when destination country is set countryRepository setDestinationCountry is called`() {
        countrySelectionUseCase.setDestinationCountry(destinationCountry)
        verify(countryRepository, times(1)).setDestinationCountry(destinationCountry)
    }

    @Test
    fun `when free user is created loginRepository getLimitedLogin is called`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginSuccess)
        countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        verify(userDataRepository, times(1)).getLimitedLogin(originCountry, destinationCountry)
    }

    @Test
    fun `when free user is created userTokenLocal setUserToken is called`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginSuccess)
        countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        verify(userDataRepository, times(1)).getLimitedLogin(originCountry, destinationCountry)
    }

    @Test
    fun `when free user is created userDataRepository setLimitedUser is called`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginSuccess)
        countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        verify(userDataRepository, times(1)).setLoggedUser(limitedUser)
    }

    @Test
    fun `when free user is created userDataRepository setUser is called`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginSuccess)
        countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        verify(userDataRepository, times(1)).setLoggedUser(limitedUser)
    }

    @Test
    fun `when free user is created createFreeUser returns success`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginSuccess)
        val result = countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        val expectedResult = Success(limitedUser)
        assertEquals(result, expectedResult)
    }

    @Test
    fun `when free user is not created createFreeUser returns error`() {
        `when`(userDataRepository.getLimitedLogin(originCountry, destinationCountry)).thenReturn(limitedLoginFailure)
        val result = countrySelectionUseCase.createFreeUser(originCountry, destinationCountry)
        val expectedResult = limitedLoginFailure
        assertEquals(expectedResult, result)
    }
}
