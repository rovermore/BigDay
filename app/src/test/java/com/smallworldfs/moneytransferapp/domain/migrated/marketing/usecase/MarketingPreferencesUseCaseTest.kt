package com.smallworldfs.moneytransferapp.domain.migrated.marketing.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.FormMock
import com.smallworldfs.moneytransferapp.mocks.dto.MarketingPreferenceDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MarketingPreferencesUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    private lateinit var marketingPreferencesUseCase: MarketingPreferencesUseCase

    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))
    private val formView = "form view"
    private val form = FormMock.form
    private val email = true
    private val sms = true
    private val push = true
    private val marketingPreferencesDTO = MarketingPreferenceDTOMock.marketingPreferencesDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        marketingPreferencesUseCase = MarketingPreferencesUseCase(
            userDataRepository
        )
    }

    @Test
    fun `when requestPreferences success userDataRepository requestMarketingPreferences is called`() {
        Mockito.`when`(userDataRepository.requestMarketingPreferences(formView)).thenReturn(Success(form))
        marketingPreferencesUseCase.requestPreferences(formView)
        Mockito.verify(userDataRepository, Mockito.times(1)).requestMarketingPreferences(formView)
    }

    @Test
    fun `when requestPreferences success userDataRepository requestMarketingPreferences returns success`() {
        Mockito.`when`(userDataRepository.requestMarketingPreferences(formView)).thenReturn(Success(form))
        val result = marketingPreferencesUseCase.requestPreferences(formView)
        Assert.assertEquals(Success(form), result)
    }

    @Test
    fun `when requestPreferences failure userDataRepository requestMarketingPreferences returns error`() {
        Mockito.`when`(userDataRepository.requestMarketingPreferences(formView)).thenReturn(error)
        val result = marketingPreferencesUseCase.requestPreferences(formView)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when updatePreferences success userDataRepository updateMarketingPreferences is called`() {
        Mockito.`when`(userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)).thenReturn(Success(Unit))
        marketingPreferencesUseCase.updatePreferences(marketingPreferencesDTO)
        Mockito.verify(userDataRepository, Mockito.times(1)).updateMarketingPreferences(marketingPreferencesDTO)
    }

    @Test
    fun `when updatePreferences success userDataRepository updateMarketingPreferences returns success`() {
        Mockito.`when`(userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)).thenReturn(Success(Unit))
        val result = marketingPreferencesUseCase.updatePreferences(marketingPreferencesDTO)
        Assert.assertEquals(Success(Unit), result)
    }

    @Test
    fun `when updatePreferences failure userDataRepository updateMarketingPreferences returns error`() {
        Mockito.`when`(userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)).thenReturn(error)
        val result = marketingPreferencesUseCase.updatePreferences(marketingPreferencesDTO)
        Assert.assertEquals(error, result)
    }
}
