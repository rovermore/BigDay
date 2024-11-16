package com.smallworldfs.moneytransferapp.domain.migrated.form.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.form.FormSelectorUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.FormRepository
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.Source
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.utils.UrlUtils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class FormSelectorUseCaseTest {

    @Mock
    lateinit var formRepository: FormRepository

    @Mock
    lateinit var urlUtils: UrlUtils

    lateinit var formSelectorUseCase: FormSelectorUseCase

    private val baseUrl = "https://www.test.com/"
    private val testUrl = "testurl"
    private val params = arrayListOf("param1", "param2")
    private val aux = ""
    private val validSource = Source.API(
        baseUrl,
        params,
        aux
    )
    private val invalidSource = Source.Local
    private val successFieldContent = Success(listOf(FormSelectorItem()))
    private val errorInvalidSource = Failure(Error.UncompletedOperation("Uncompleted source cast"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        formSelectorUseCase = FormSelectorUseCase(
            formRepository,
            urlUtils
        )
    }

    @Test
    fun `when source is correct urlUtils formUrlFromApiRef is called`() {
        `when`(
            urlUtils.formUrlFromApiRef(
                baseUrl,
                params,
                aux
            )
        ).thenReturn(testUrl)
        formSelectorUseCase.getFieldContent(validSource)
        verify(urlUtils, times(1)).formUrlFromApiRef(
            baseUrl,
            params,
            aux
        )
    }

    @Test
    fun `when urlUtils formUrlFromApiRef is correct formRepository getFieldContentFromNetwork is called`() {
        `when`(
            urlUtils.formUrlFromApiRef(
                baseUrl,
                params,
                aux
            )
        ).thenReturn(testUrl)
        `when`(formRepository.getFieldContentFromNetwork(testUrl)).thenReturn(successFieldContent)
        formSelectorUseCase.getFieldContent(validSource)
        verify(formRepository, times(1)).getFieldContentFromNetwork(testUrl)
    }

    @Test
    fun `when source is correct getFieldContent returns success`() {
        `when`(
            urlUtils.formUrlFromApiRef(
                baseUrl,
                params,
                aux
            )
        ).thenReturn(testUrl)
        `when`(formRepository.getFieldContentFromNetwork(testUrl)).thenReturn(successFieldContent)
        val result = formSelectorUseCase.getFieldContent(validSource)
        val expectedResult = Success(listOf(FormSelectorItem()))
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when source is wrong getFieldContent returns error`() {
        val result = formSelectorUseCase.getFieldContent(invalidSource)
        val expectedResult = errorInvalidSource.reason.message
        assertEquals(expectedResult, (result.get() as Error.UncompletedOperation).message)
    }
}
