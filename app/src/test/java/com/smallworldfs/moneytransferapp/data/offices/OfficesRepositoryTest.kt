package com.smallworldfs.moneytransferapp.data.offices

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.model.CityDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.model.OfficeDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.network.OfficeNetworkDatasource
import com.smallworldfs.moneytransferapp.data.offices.repository.OfficesRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.offices.repository.OfficesRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.CitiesDTO
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.OfficesDTOListMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.CityResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.OfficeCountryResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.OfficesPoiResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.OfficesResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OfficesRepositoryTest {

    @Mock
    lateinit var officeNetworkDatasource: OfficeNetworkDatasource

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var countriesDTOMapper: CountriesDTOMapper

    @Mock
    lateinit var cityDTOMapper: CityDTOMapper

    @Mock
    lateinit var officeDTOMapper: OfficeDTOMapper

    lateinit var officesRepository: OfficesRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedApiErrorFailure = Failure(unmappedApiError)
    private val unmappedDomainError = Error.Unmapped("Unmapped error")
    private val unregisteredUserDomainError = Error.UnregisteredUser("No user found in device")

    private val country = "es"
    private val city = "madrid"
    private val longitude = "45.034345"
    private val latitude = "-0.454001"

    private val userDTO = UserDTOMock.userDTO
    private val userDTOSuccess = Success(userDTO)

    private val CONTRY_MAPCOUNTRIES_TYPE = "mapcountries"

    private val officeCountryResponse = OfficeCountryResponseMock.officeCountryResponse
    private val countriesDTO = CountriesDTOMock.countriesDTO
    private val cityResponseMock = CityResponseMock.cityResponseMock
    private val cityDTOList = CitiesDTO.cityDTOList
    private val officesResponse = OfficesResponseMock.officesResponse
    private val officeDTOList = OfficesDTOListMock.officeDTOList
    private val officesPoiResponse = OfficesPoiResponseMock.officesPoiResponse

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        officesRepository = OfficesRepositoryImpl(
            officeNetworkDatasource,
            userDataRepository,
            apiErrorMapper,
            countriesDTOMapper,
            cityDTOMapper,
            officeDTOMapper
        )
    }

    @Test
    fun `when requestCountries success officeNetworkDatasource requestCountries is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(countriesDTOMapper.mapFromOfficeCountryResponse(officeCountryResponse))
            .thenReturn(countriesDTO)
        Mockito.`when`(
            officeNetworkDatasource.requestCountries(
                CONTRY_MAPCOUNTRIES_TYPE,
                userDTO.country.countries.first().iso3,
            )
        ).thenReturn(Success(officeCountryResponse))
        officesRepository.requestCountries()
        Mockito.verify(officeNetworkDatasource, Mockito.times(1))
            .requestCountries(
                CONTRY_MAPCOUNTRIES_TYPE,
                userDTO.country.countries.first().iso3,
            )
    }

    @Test
    fun `when requestCountries success officeNetworkDatasource requestCountries returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(countriesDTOMapper.mapFromOfficeCountryResponse(officeCountryResponse))
            .thenReturn(countriesDTO)
        Mockito.`when`(
            officeNetworkDatasource.requestCountries(
                CONTRY_MAPCOUNTRIES_TYPE,
                userDTO.country.countries.first().iso3,
            )
        ).thenReturn(Success(officeCountryResponse))
        val result = officesRepository.requestCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when requestCountries failure officeNetworkDatasource requestCountries returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(countriesDTOMapper.mapFromOfficeCountryResponse(officeCountryResponse))
            .thenReturn(countriesDTO)
        Mockito.`when`(
            officeNetworkDatasource.requestCountries(
                CONTRY_MAPCOUNTRIES_TYPE,
                userDTO.country.countries.first().iso3,
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = officesRepository.requestCountries()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestCountries failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = officesRepository.requestCountries()
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when requestCities success officeNetworkDatasource requestCities is called`() {
        Mockito.`when`(cityDTOMapper.map(cityResponseMock.cities))
            .thenReturn(cityDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestCities(
                country,
            )
        ).thenReturn(Success(cityResponseMock))
        officesRepository.requestCities(country)
        Mockito.verify(officeNetworkDatasource, Mockito.times(1))
            .requestCities(
                country,
            )
    }

    @Test
    fun `when requestCities success officeNetworkDatasource requestCities returns success`() {
        Mockito.`when`(cityDTOMapper.map(cityResponseMock.cities)).thenReturn(cityDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestCities(
                country,
            )
        ).thenReturn(Success(cityResponseMock))
        val result = officesRepository.requestCities(country)
        Assert.assertEquals(Success(cityDTOList), result)
    }

    @Test
    fun `when requestCities failure officeNetworkDatasource requestCities returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(cityDTOMapper.map(cityResponseMock.cities)).thenReturn(cityDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestCities(
                country,
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = officesRepository.requestCities(country)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestOffices success officeNetworkDatasource requestOffices is called`() {
        Mockito.`when`(officeDTOMapper.mapFromBranches(officesResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestOffices(
                country,
                city,
            )
        ).thenReturn(Success(officesResponse))
        officesRepository.requestOffices(country, city)
        Mockito.verify(officeNetworkDatasource, Mockito.times(1))
            .requestOffices(
                country,
                city,
            )
    }

    @Test
    fun `when requestOffices success officeNetworkDatasource requestOffices returns success`() {
        Mockito.`when`(officeDTOMapper.mapFromBranches(officesResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestOffices(
                country,
                city,
            )
        ).thenReturn(Success(officesResponse))
        val result = officesRepository.requestOffices(country, city)
        Assert.assertEquals(Success(officeDTOList), result)
    }

    @Test
    fun `when requestOffices failure officeNetworkDatasource requestOffices returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(officeDTOMapper.mapFromBranches(officesResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestOffices(
                country,
                city,
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = officesRepository.requestOffices(country, city)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestOfficesPoi success officeNetworkDatasource requestPoi is called`() {
        Mockito.`when`(officeDTOMapper.mapFromLocations(officesPoiResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestPoi(
                longitude,
                latitude,
            )
        ).thenReturn(Success(officesPoiResponse))
        officesRepository.requestOfficesPoi(longitude, latitude)
        Mockito.verify(officeNetworkDatasource, Mockito.times(1))
            .requestPoi(
                longitude,
                latitude,
            )
    }

    @Test
    fun `when requestOfficesPoi success officeNetworkDatasource requestPoi returns success`() {
        Mockito.`when`(officeDTOMapper.mapFromLocations(officesPoiResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestPoi(
                longitude,
                latitude,
            )
        ).thenReturn(Success(officesPoiResponse))
        val result = officesRepository.requestOfficesPoi(longitude, latitude)
        Assert.assertEquals(Success(officeDTOList), result)
    }

    @Test
    fun `when requestOfficesPoi failure officeNetworkDatasource requestPoi returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(officeDTOMapper.mapFromLocations(officesPoiResponse))
            .thenReturn(officeDTOList)
        Mockito.`when`(
            officeNetworkDatasource.requestPoi(
                longitude,
                latitude,
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = officesRepository.requestOfficesPoi(longitude, latitude)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
