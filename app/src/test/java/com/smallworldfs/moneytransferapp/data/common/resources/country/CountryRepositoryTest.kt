package com.smallworldfs.moneytransferapp.data.common.resources.country

import com.smallworldfs.moneytransferapp.data.CountryResponseMock
import com.smallworldfs.moneytransferapp.data.GetDestinationCountriesResponseMock
import com.smallworldfs.moneytransferapp.data.GetOriginCountriesResponseMock
import com.smallworldfs.moneytransferapp.data.GetStatesResponseMock
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesDTOMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesEntityMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryListEntity
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.StatesDTOMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.CountryRepositoryImpl
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.local.CountryLocalDataSource
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network.CountryNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.StateDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CountryRepositoryTest {

    @Mock
    lateinit var countryLocalDataSource: CountryLocalDataSource

    @Mock
    lateinit var countryNetworkDatasource: CountryNetworkDatasource

    @Mock
    lateinit var localeRepository: LocaleRepository

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var countriesDTOMapper: CountriesDTOMapper

    @Mock
    lateinit var statesDTOMapper: StatesDTOMapper

    @Mock
    lateinit var countriesEntityMapper: CountriesEntityMapper

    lateinit var countryRepository: CountryRepositoryImpl

    private val apiError = Failure(APIError.UnmappedError(432, "Unmapped error"))
    private val error = Failure(Error.UnregisteredUser("User not found"))

    private val localCountryListEntity = CountryListEntity()
    private val successLocalCountryListEntity = Success(localCountryListEntity)
    private val countriesDTO = CountriesDTOMock.countriesDTO
    private val countryDTO = CountriesDTOMock.countriesDTO.countries[0]
    private val countryResponse = CountryResponseMock.countryResponse
    private val successCountryResponse = Success(countryResponse)
    private val originCountryResponse = Success(GetOriginCountriesResponseMock.originCountryResponse)
    private val destinationResponse = Success(GetDestinationCountriesResponseMock.destinationResponse)
    private val statesResponse = Success(GetStatesResponseMock.statesResponse)
    private val statesDTOList = Success(StateDTOMock.stateList)
    private val countryFromPhone = Success("country from phone")

    private val lang = "en"
    private val lat = 45.4503
    private val long = 0.4503
    private val origin = "origin"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        countryRepository = CountryRepositoryImpl(
            countryLocalDataSource,
            countryNetworkDatasource,
            localeRepository,
            apiErrorMapper,
            countriesDTOMapper,
            statesDTOMapper,
            countriesEntityMapper
        )
    }

    @Test
    fun `when getCountries success countryNetworkDatasource requestInfo is called`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countriesDTOMapper.mapFromLocalEntity(successLocalCountryListEntity.value))
            .thenReturn(countriesDTO)

        countryRepository.getCountries()
        Mockito.verify(countriesDTOMapper, Mockito.times(1))
            .mapFromLocalEntity(successLocalCountryListEntity.value)
    }

    @Test
    fun `when getCountries success countryNetworkDatasource requestInfo returns success`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countriesDTOMapper.mapFromLocalEntity(successLocalCountryListEntity.value))
            .thenReturn(countriesDTO)

        val result = countryRepository.getCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getCountries failure countryLocalDataSource getCountries  returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(error)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(countryNetworkDatasource.requestInfo(localeRepository.getLang()))
            .thenReturn(successCountryResponse)
        Mockito.`when`(countriesDTOMapper.mapFromNetworkCountries(successCountryResponse.value))
            .thenReturn(countriesDTO)

        val result = countryRepository.getCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getCountries failure countryNetworkDatasource request returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(error)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(countryNetworkDatasource.requestInfo(localeRepository.getLang()))
            .thenReturn(apiError)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(error.reason)

        val result = countryRepository.getCountries()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getOriginCountries success countryLocalDataSource getOriginCountries is called`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countryLocalDataSource.getOriginCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countriesDTOMapper.mapFromLocalEntity(successLocalCountryListEntity.value))
            .thenReturn(countriesDTO)

        countryRepository.getOriginCountries()
        Mockito.verify(countriesDTOMapper, Mockito.times(1))
            .mapFromLocalEntity(successLocalCountryListEntity.value)
    }

    @Test
    fun `when getOriginCountries success countryLocalDataSource getOriginCountries return success`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countryLocalDataSource.getOriginCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countriesDTOMapper.mapFromLocalEntity(successLocalCountryListEntity.value))
            .thenReturn(countriesDTO)

        val result = countryRepository.getOriginCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getOriginCountries success countryNetworkDatasource requestCountriesOrigin returns success`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(countryLocalDataSource.getOriginCountries()).thenReturn(error)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(countryNetworkDatasource.requestCountriesOrigin(localeRepository.getLang()))
            .thenReturn(originCountryResponse)
        Mockito.`when`(countriesDTOMapper.mapFromNetworkOriginCountries(originCountryResponse.value, successLocalCountryListEntity.value))
            .thenReturn(countriesDTO)

        val result = countryRepository.getOriginCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getOriginCountries failure countryNetworkDatasource requestCountriesOrigin returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(error)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(countryNetworkDatasource.requestInfo(localeRepository.getLang()))
            .thenReturn(apiError)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(error.reason)

        val result = countryRepository.getCountries()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getOriginCountries with params success countryNetworkDatasource getOriginCountries is called`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestCountriesOrigin(
                localeRepository.getLang(),
                lat,
                long
            )
        ).thenReturn(originCountryResponse)
        Mockito.`when`(
            countriesDTOMapper.mapFromNetworkOriginCountries(
                originCountryResponse.value,
                successLocalCountryListEntity.value
            )
        )
            .thenReturn(countriesDTO)

        countryRepository.getOriginCountries(lat, long)
        Mockito.verify(countriesDTOMapper, Mockito.times(1))
            .mapFromNetworkOriginCountries(
                originCountryResponse.value,
                successLocalCountryListEntity.value
            )
    }

    @Test
    fun `when getOriginCountries with params success countryNetworkDatasource requestCountriesOrigin returns success`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestCountriesOrigin(
                localeRepository.getLang(),
                lat,
                long
            )
        ).thenReturn(originCountryResponse)
        Mockito.`when`(
            countriesDTOMapper.mapFromNetworkOriginCountries(
                originCountryResponse.value,
                successLocalCountryListEntity.value
            )
        )
            .thenReturn(countriesDTO)

        val result = countryRepository.getOriginCountries(lat, long)
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getOriginCountries with params failure countryNetworkDatasource requestCountriesOrigin returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestCountriesOrigin(
                localeRepository.getLang(),
                lat,
                long
            )
        ).thenReturn(apiError)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(error.reason)

        val result = countryRepository.getOriginCountries(lat, long)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getDestinationCountries with params success countryNetworkDatasource requestDestinationCountries is called`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestDestinationCountries(
                localeRepository.getLang(),
                origin
            )
        ).thenReturn(destinationResponse)
        Mockito.`when`(
            countriesDTOMapper.mapFromNetworkDestinationCountries(
                destinationResponse.value,
                successLocalCountryListEntity.value
            )
        )
            .thenReturn(countriesDTO)

        countryRepository.getDestinationCountries(origin)
        Mockito.verify(countriesDTOMapper, Mockito.times(1))
            .mapFromNetworkDestinationCountries(
                destinationResponse.value,
                successLocalCountryListEntity.value
            )
    }

    @Test
    fun `when getDestinationCountries with params success countryNetworkDatasource requestDestinationCountries returns success`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestDestinationCountries(
                localeRepository.getLang(),
                origin
            )
        ).thenReturn(destinationResponse)
        Mockito.`when`(
            countriesDTOMapper.mapFromNetworkDestinationCountries(
                destinationResponse.value,
                successLocalCountryListEntity.value
            )
        )
            .thenReturn(countriesDTO)

        val result = countryRepository.getDestinationCountries(origin)
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getDestinationCountries with params failure countryNetworkDatasource requestDestinationCountries returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountries()).thenReturn(successLocalCountryListEntity)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.requestDestinationCountries(
                localeRepository.getLang(),
                origin
            )
        ).thenReturn(apiError)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(error.reason)

        val result = countryRepository.getDestinationCountries(origin)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getStates with params success countryNetworkDatasource getStates is called`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.getStates(
                localeRepository.getLang(),
                countryDTO.iso3
            )
        ).thenReturn(statesResponse)
        Mockito.`when`(
            statesDTOMapper.mapFromNetworkStates(
                statesResponse.value
            )
        )
            .thenReturn(statesDTOList.value)

        countryRepository.getStates(countryDTO)
        Mockito.verify(statesDTOMapper, Mockito.times(1))
            .mapFromNetworkStates(
                statesResponse.value
            )
    }

    @Test
    fun `when getStates with params success countryNetworkDatasource getStates returns success`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.getStates(
                localeRepository.getLang(),
                countryDTO.iso3
            )
        ).thenReturn(statesResponse)
        Mockito.`when`(
            statesDTOMapper.mapFromNetworkStates(
                statesResponse.value
            )
        )
            .thenReturn(statesDTOList.value)

        val result = countryRepository.getStates(countryDTO)
        Assert.assertEquals(statesDTOList, result)
    }

    @Test
    fun `when getStates with params failure countryNetworkDatasource getStates returns error`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            countryNetworkDatasource.getStates(
                localeRepository.getLang(),
                countryDTO.iso3
            )
        ).thenReturn(apiError)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(error.reason)

        val result = countryRepository.getStates(countryDTO)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getCountryFromPhoneNumber with params success countryNetworkDatasource getCountryFromPhoneNumber is called`() {
        Mockito.`when`(countryLocalDataSource.getCountryFromPhoneNumber())
            .thenReturn(countryFromPhone)

        countryRepository.getCountryFromPhoneNumber()
        Mockito.verify(countryLocalDataSource, Mockito.times(1))
            .getCountryFromPhoneNumber()
    }

    @Test
    fun `when getCountryFromPhoneNumber with params success countryNetworkDatasource getCountryFromPhoneNumber returns success`() {
        Mockito.`when`(countryLocalDataSource.getCountryFromPhoneNumber())
            .thenReturn(countryFromPhone)

        val result = countryRepository.getCountryFromPhoneNumber()
        Assert.assertEquals(countryFromPhone, result)
    }

    @Test
    fun `when getCountryFromPhoneNumber with params failure countryNetworkDatasource getCountryFromPhoneNumber returns error`() {
        Mockito.`when`(countryLocalDataSource.getCountryFromPhoneNumber()).thenReturn(error)

        val result = countryRepository.getCountryFromPhoneNumber()
        Assert.assertEquals(error, result)
    }
}
