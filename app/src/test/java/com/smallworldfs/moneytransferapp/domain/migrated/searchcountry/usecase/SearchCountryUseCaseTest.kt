package com.smallworldfs.moneytransferapp.domain.migrated.searchcountry.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.searchcountry.SearchCountryUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchCountryUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var countryRepository: CountryRepository

    private lateinit var searchCountryUseCase: SearchCountryUseCase

    private val countriesDTO = CountriesDTO(mutableListOf(CountryDTO("ESP", "España"), CountryDTO("FRA", "Francia")))
    private val successCountry = Success(countriesDTO)
    private val error = Failure(Error.UncompletedOperation("operation could note be completed"))
    private val user = UserDTOMock.userDTO
    private val userSuccess = Success(user)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        searchCountryUseCase = SearchCountryUseCase(
            countryRepository,
            userDataRepository
        )
    }

    @Test
    fun `when getDestinationCountries success countryRepository getDestinationCountries is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(countryRepository.getDestinationCountries(user.country.countries.first().iso3)).thenReturn(successCountry)
        searchCountryUseCase.getDestinationCountries()
        Mockito.verify(countryRepository, Mockito.times(1)).getDestinationCountries(user.country.countries.first().iso3)
    }

    @Test
    fun `when getDestinationCountries success countryRepository getDestinationCountries returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(countryRepository.getDestinationCountries(user.country.countries.first().iso3)).thenReturn(successCountry)
        val result = searchCountryUseCase.getDestinationCountries()
        Assert.assertEquals(successCountry, result)
    }

    @Test
    fun `when getDestinationCountries failure countryRepository getDestinationCountries returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(countryRepository.getDestinationCountries(user.country.countries.first().iso3)).thenReturn(error)
        val result = searchCountryUseCase.getDestinationCountries()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getDestinationCountries failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(error)
        Mockito.`when`(countryRepository.getDestinationCountries(user.country.countries.first().iso3)).thenReturn(successCountry)
        val result = searchCountryUseCase.getDestinationCountries()
        Assert.assertEquals(error, result)
    }
}
