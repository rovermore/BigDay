package com.smallworldfs.moneytransferapp.data.form

import com.smallworldfs.moneytransferapp.mocks.response.FormContentMock
import com.smallworldfs.moneytransferapp.mocks.response.FormSelectorItemMock
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.form.network.FormNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.FormRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FormRepositoryTest {

    @Mock
    lateinit var formNetworkDatasource: FormNetworkDatasource

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var formRepository: FormRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedApiErrorFailure = Failure(unmappedApiError)
    private val unmappedDomainError = Error.Unmapped("Unmapped error")

    private val formContent = FormContentMock.formContent
    private val url = "url"
    private val formSelectorItemList = FormSelectorItemMock.formSelectorItemList

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        formRepository = FormRepositoryImpl(
            formNetworkDatasource,
            apiErrorMapper
        )
    }

    @Test
    fun `when getFieldContentFromNetwork success formNetworkDatasource getFieldContent is called`() {
        Mockito.`when`(formNetworkDatasource.getFieldContent(url)).thenReturn(Success(formContent))
        formRepository.getFieldContentFromNetwork(url)
        Mockito.verify(formNetworkDatasource, Mockito.times(1))
            .getFieldContent(url)
    }

    @Test
    fun `when getFieldContentFromNetwork success formNetworkDatasource getFieldContent returns success`() {
        Mockito.`when`(formNetworkDatasource.getFieldContent(url)).thenReturn(Success(formContent))
        val result = formRepository.getFieldContentFromNetwork(url)
        Assert.assertEquals(
            Success(formSelectorItemList).javaClass.simpleName,
            result.javaClass.simpleName
        )
    }

    @Test
    fun `when getFieldContentFromNetwork failure formNetworkDatasource getFieldContent returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(formNetworkDatasource.getFieldContent(url)).thenReturn(unmappedApiErrorFailure)
        val result = formRepository.getFieldContentFromNetwork(url)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
