package com.smallworldfs.moneytransferapp.domain.migrated.mtn.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository.MTNRepository
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.MtnStatusDTOMock
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MTNUseCaseTest {

    @Mock
    lateinit var countryRepository: CountryRepository

    @Mock
    lateinit var mtnRepository: MTNRepository

    private lateinit var mtnUseCase: MTNUseCase

    private val error = Failure(Error.UncompletedOperation(""))

    private val mtnStatusDTO = MtnStatusDTOMock.mtnStatusDTO
    private val mtn = "mtn"
    private val country = "country"

    private val countriesDTO = CountriesDTOMock.countriesDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        mtnUseCase = MTNUseCase(
            countryRepository,
            mtnRepository,
        )
    }

    @Test
    fun `when getMtnStatus success mtnRepository getMtnStatus is called`() {
        Mockito.`when`(mtnRepository.getMtnStatus(mtn, country)).thenReturn(Success(mtnStatusDTO))

        mtnUseCase.getMtnStatus(mtn, country)
        Mockito.verify(mtnRepository, Mockito.times(1)).getMtnStatus(mtn, country)
    }

    @Test
    fun `when getMtnStatus success mtnRepository getMtnStatus returns success`() {
        Mockito.`when`(mtnRepository.getMtnStatus(mtn, country)).thenReturn(Success(mtnStatusDTO))

        val result = mtnUseCase.getMtnStatus(mtn, country)
        Assert.assertEquals(Success(mtnStatusDTO), result)
    }

    @Test
    fun `when getMtnStatus failure mtnRepository getMtnStatus returns error`() {
        Mockito.`when`(mtnRepository.getMtnStatus(mtn, country)).thenReturn(error)

        val result = mtnUseCase.getMtnStatus(mtn, country)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getCountries success countryRepository getCountries is called`() {
        Mockito.`when`(countryRepository.getCountries()).thenReturn(Success(countriesDTO))

        mtnUseCase.getCountries()
        Mockito.verify(countryRepository, Mockito.times(1)).getCountries()
    }

    @Test
    fun `when getCountries success countryRepository getCountries returns success`() {
        Mockito.`when`(countryRepository.getCountries()).thenReturn(Success(countriesDTO))

        val result = mtnUseCase.getCountries()
        Assert.assertEquals(Success(countriesDTO), result)
    }

    @Test
    fun `when getCountries failure countryRepository getCountries returns error`() {
        Mockito.`when`(countryRepository.getCountries()).thenReturn(error)

        val result = mtnUseCase.getCountries()
        Assert.assertEquals(error, result)
    }
}
