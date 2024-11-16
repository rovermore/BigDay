package com.smallworldfs.moneytransferapp.domain.migrated.status.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.TranslationsMock
import com.smallworldfs.moneytransferapp.mocks.dto.DeliveryMethodDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.StatusTransactionDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class StatusUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var statusRepository: StatusRepository

    @Mock
    lateinit var calculatorRepository: CalculatorRepository

    private lateinit var statusUseCase: StatusUseCase

    private val error = Failure(Error.UncompletedOperation(""))

    private val user = UserDTOMock.userDTO
    private val userSuccess = Success(user)
    private val userError = Failure(Error.UnregisteredUser("No user found in device"))

    private val paymentMethodTranslated = TranslationsMock.paymentMethodTranslated
    private val successPaymentMethodTranslated = Success(paymentMethodTranslated)
    private val deliveryMethodsTranslated = DeliveryMethodDTOMock.deliveryMethodsTranslated.toList()
    private val successDeliveryMethodsTranslated = Success(deliveryMethodsTranslated)
    private val statusTransactionDTO = StatusTransactionDTOMock.statusTransactionDTO
    private val successStatusTransactionDTO = Success(statusTransactionDTO)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        statusUseCase = StatusUseCase(
            statusRepository,
            userDataRepository,
            calculatorRepository
        )
    }

    @Test
    fun `when getTransactions success statusRepository getTranslatePaymentMethods is called`() {
        `when`(statusUseCase.getUser()).thenReturn(userSuccess)
        `when`(statusRepository.getTranslatePaymentMethods()).thenReturn(successPaymentMethodTranslated)
        `when`(calculatorRepository.getDeliveryMethods()).thenReturn(successDeliveryMethodsTranslated)
        `when`(statusRepository.getTransactions()).thenReturn(successStatusTransactionDTO)

        statusUseCase.getTransactions()
        verify(statusRepository, times(1)).getTransactions()
    }

    @Test
    fun `when getTransactions success statusRepository getTranslatePaymentMethods returns success`() {
        `when`(statusUseCase.getUser()).thenReturn(userSuccess)
        `when`(statusRepository.getTranslatePaymentMethods()).thenReturn(successPaymentMethodTranslated)
        `when`(calculatorRepository.getDeliveryMethods()).thenReturn(successDeliveryMethodsTranslated)
        `when`(statusRepository.getTransactions()).thenReturn(successStatusTransactionDTO)

        val result = statusUseCase.getTransactions()
        assertEquals(successStatusTransactionDTO, result)
    }

    @Test
    fun `when getTransactions failure calculatorRepository getDeliveryMethods returns error`() {
        `when`(statusUseCase.getUser()).thenReturn(userSuccess)
        `when`(statusRepository.getTranslatePaymentMethods()).thenReturn(successPaymentMethodTranslated)
        `when`(calculatorRepository.getDeliveryMethods()).thenReturn(error)
        `when`(statusRepository.getTransactions()).thenReturn(successStatusTransactionDTO)

        val result = statusUseCase.getTransactions()
        assertEquals(error, result)
    }

    @Test
    fun `when getTransactions failure statusRepository getTranslatePaymentMethods returns error`() {
        `when`(statusUseCase.getUser()).thenReturn(userSuccess)
        `when`(statusRepository.getTranslatePaymentMethods()).thenReturn(error)
        `when`(calculatorRepository.getDeliveryMethods()).thenReturn(successDeliveryMethodsTranslated)
        `when`(statusRepository.getTransactions()).thenReturn(successStatusTransactionDTO)

        val result = statusUseCase.getTransactions()
        assertEquals(error, result)
    }

    @Test
    fun `when getTransactions failure statusRepository getTransactions returns error`() {
        `when`(statusUseCase.getUser()).thenReturn(userSuccess)
        `when`(statusRepository.getTranslatePaymentMethods()).thenReturn(error)
        `when`(calculatorRepository.getDeliveryMethods()).thenReturn(successDeliveryMethodsTranslated)
        `when`(statusRepository.getTransactions()).thenReturn(error)

        val result = statusUseCase.getTransactions()
        assertEquals(error, result)
    }

    @Test
    fun `when getUser success userDataRepository getLoggedUser is called`() {
        statusUseCase.getUser()
        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when getUser success userDataRepository getLoggedUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        val result = statusUseCase.getUser()
        assertEquals(userSuccess, result)
    }

    @Test
    fun `when getUser failure userDataRepository getLoggedUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(userError)
        val result = statusUseCase.getUser()
        assertEquals(userError, result)
    }
}
