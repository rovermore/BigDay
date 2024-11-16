package com.smallworldfs.moneytransferapp.domain.migrated.offices.usecase

import com.smallworldfs.moneytransferapp.mocks.SWCoordinatesMock
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.repository.OfficesRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.OfficesDTOListMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OfficesUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var officesRepository: OfficesRepository

    @Mock
    lateinit var locationRepository: LocationRepository

    @Mock
    lateinit var capabilityChecker: CapabilityChecker

    lateinit var officesUseCase: OfficesUseCase

    private val countriesDTO = CountriesDTO(mutableListOf(CountryDTO("ESP", "España"), CountryDTO("FRA", "Francia")))
    private val successCountry = Success(countriesDTO)
    private val error = Failure(Error.UncompletedOperation("operation could note be completed"))
    private val country = "ESP"
    private val city = "Madrid"
    private val cityDTOList = listOf(CityDTO("Madrid"), CityDTO("Barcelona"))
    private val successCityDTOList = Success(cityDTOList)
    private val user = UserDTOMock.userDTO
    private val loginSuccess = Success(user)
    private val officeDTOList = OfficesDTOListMock.officeDTOList
    private val successListOfficeDTO = Success(officeDTOList)
    private val longitude = "0.0"
    private val latitude = "0.0"

    private val coordinates = SWCoordinatesMock.coordinates
    private val coordinates2 = SWCoordinatesMock.coordinates2
    private val distance = "9,8 km"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        officesUseCase = OfficesUseCase(
            officesRepository,
            userDataRepository,
            locationRepository,
            capabilityChecker
        )
    }

    @Test
    fun `when getAllOfficeCountries success officesRepository requestCountries is called`() {
        officesUseCase.getAllOfficeCountries()
        Mockito.verify(officesRepository, Mockito.times(1)).requestCountries()
    }
    @Test
    fun `when getAllOfficeCountries success officesRepository requestCountries returns success`() {
        Mockito.`when`(officesRepository.requestCountries()).thenReturn(successCountry)
        val result = officesUseCase.getAllOfficeCountries()
        Assert.assertEquals(successCountry, result)
    }

    @Test
    fun `when getAllOfficeCountries failure officesRepository requestCountries returns error`() {
        Mockito.`when`(officesRepository.requestCountries()).thenReturn(error)
        val result = officesUseCase.getAllOfficeCountries()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getCities success officesRepository requestCities is called`() {
        officesUseCase.getCities(country)
        Mockito.verify(officesRepository, Mockito.times(1)).requestCities(country)
    }
    @Test
    fun `when getCities success officesRepository requestCities returns success`() {
        Mockito.`when`(officesRepository.requestCities(country)).thenReturn(successCityDTOList)
        val result = officesUseCase.getCities(country)
        Assert.assertEquals(successCityDTOList, result)
    }

    @Test
    fun `when getCities failure officesRepository requestCities returns error`() {
        Mockito.`when`(officesRepository.requestCities(country)).thenReturn(error)
        val result = officesUseCase.getCities(country)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getUser success userDataRepository getUser is called`() {
        officesUseCase.getUser()
        Mockito.verify(userDataRepository, Mockito.times(1)).getLoggedUser()
    }
    @Test
    fun `when getUser success userDataRepository getUser returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(loginSuccess)
        val result = officesUseCase.getUser()
        Assert.assertEquals(loginSuccess, result)
    }

    @Test
    fun `when getUser failure userDataRepository getUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = officesUseCase.getUser()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getOffices success officesRepository requestOffices is called`() {
        officesUseCase.getOffices(country, city)
        Mockito.verify(officesRepository, Mockito.times(1)).requestOffices(country, city)
    }
    @Test
    fun `when getOffices success officesRepository requestOffices returns success`() {
        Mockito.`when`(officesRepository.requestOffices(country, city)).thenReturn(successListOfficeDTO)
        val result = officesUseCase.getOffices(country, city)
        Assert.assertEquals(successListOfficeDTO, result)
    }

    @Test
    fun `when getOffices failure officesRepository requestOffices returns error`() {
        Mockito.`when`(officesRepository.requestOffices(country, city)).thenReturn(error)
        val result = officesUseCase.getOffices(country, city)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getOfficesPoi success officesRepository requestOfficesPoi is called`() {
        officesUseCase.getOfficesPoi(longitude, latitude)
        Mockito.verify(officesRepository, Mockito.times(1)).requestOfficesPoi(longitude, latitude)
    }
    @Test
    fun `when getOfficesPoi success officesRepository requestOfficesPoi returns success`() {
        Mockito.`when`(officesRepository.requestOfficesPoi(longitude, latitude)).thenReturn(successListOfficeDTO)
        val result = officesUseCase.getOfficesPoi(longitude, latitude)
        Assert.assertEquals(successListOfficeDTO, result)
    }

    @Test
    fun `when getOfficesPoi failure officesRepository requestOfficesPoi returns error`() {
        Mockito.`when`(officesRepository.requestOfficesPoi(longitude, latitude)).thenReturn(error)
        val result = officesUseCase.getOfficesPoi(longitude, latitude)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getUserLocation success locationRepository getUserLocation is called`() {
        Mockito.`when`(capabilityChecker.askForLocationPermissions()).thenReturn(Success(true))
        officesUseCase.getUserLocation()
        Mockito.verify(locationRepository, Mockito.times(1)).getUserLocation()
    }
    @Test
    fun `when getUserLocation success locationRepository getUserLocation returns success`() {
        Mockito.`when`(capabilityChecker.askForLocationPermissions()).thenReturn(Success(true))
        Mockito.`when`(locationRepository.getUserLocation()).thenReturn(Success(coordinates))
        val result = officesUseCase.getUserLocation()
        Assert.assertEquals(Success(coordinates), result)
    }

    @Test
    fun `when getUserLocation failure locationRepository getUserLocation returns error`() {
        Mockito.`when`(capabilityChecker.askForLocationPermissions()).thenReturn(Success(true))
        Mockito.`when`(locationRepository.getUserLocation()).thenReturn(error)
        val result = officesUseCase.getUserLocation()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when calculateDistance success locationRepository calculateDistance is called`() {
        officesUseCase.calculateDistance(coordinates, coordinates2)
        Mockito.verify(locationRepository, Mockito.times(1)).calculateDistance(coordinates, coordinates2)
    }
    @Test
    fun `when calculateDistance success locationRepository getUserLocation returns success`() {
        Mockito.`when`(locationRepository.calculateDistance(coordinates, coordinates2)).thenReturn(Success(distance))
        val result = officesUseCase.calculateDistance(coordinates, coordinates2)
        Assert.assertEquals(Success(distance), result)
    }

    @Test
    fun `when calculateDistance failure locationRepository getUserLocation returns error`() {
        Mockito.`when`(locationRepository.calculateDistance(coordinates, coordinates2)).thenReturn(error)
        val result = officesUseCase.calculateDistance(coordinates, coordinates2)
        Assert.assertEquals(error, result)
    }
}
