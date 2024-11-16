package com.smallworldfs.moneytransferapp.domain.migrated.status.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.FileMock
import com.smallworldfs.moneytransferapp.mocks.FormDataMock
import com.smallworldfs.moneytransferapp.mocks.dto.TransactionDetailsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TransactionStatusDetailUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var statusRepository: StatusRepository

    private lateinit var transactionStatusDetailUseCase: TransactionStatusDetailUseCase

    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))

    private val successUser = Success(UserDTOMock.userDTO)
    private val successTransactionsDetails = Success(TransactionDetailsDTOMock.transactionDetailsDTO)
    private val successCancelTransaction = Success("")
    private val successPaymentMethods = Success(FormDataMock.formData)
    private val successChangePaymentMethod = Success("")
    private val successReceipt = Success(FileMock.file)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        transactionStatusDetailUseCase = TransactionStatusDetailUseCase(
            userDataRepository,
            statusRepository
        )
    }

    @Test
    fun `when getLoggedUser success userDataRepository getLoggedUser is called`() {
        transactionStatusDetailUseCase.getLoggedUser()
        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when getLoggedUser success userDataRepository getLoggedUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        val result = transactionStatusDetailUseCase.getLoggedUser()
        assertEquals(successUser, result)
    }

    @Test
    fun `when getLoggedUser failure userDataRepository getLoggedUser returns fails`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = transactionStatusDetailUseCase.getLoggedUser()
        assertEquals(error, result)
    }

    @Test
    fun `when getTransactionDetail success statusRepository getTransactionDetail is called`() {
        transactionStatusDetailUseCase.getTransactionDetail("", false)
        verify(statusRepository, times(1)).getTransactionDetail("", false)
    }

    @Test
    fun `when getTransactionDetail success statusRepository getTransactionDetail returns success`() {
        `when`(statusRepository.getTransactionDetail("", false)).thenReturn(successTransactionsDetails)
        val result = transactionStatusDetailUseCase.getTransactionDetail("", false)
        assertEquals(successTransactionsDetails, result)
    }

    @Test
    fun `when getTransactionDetail failure statusRepository getTransactionDetail returns fails`() {
        `when`(statusRepository.getTransactionDetail("", false)).thenReturn(error)
        val result = transactionStatusDetailUseCase.getTransactionDetail("", false)
        assertEquals(error, result)
    }

    @Test
    fun `when getTransactionDetailOffline success statusRepository getTransactionDetailOffline is called`() {
        transactionStatusDetailUseCase.getTransactionDetail("", true)
        verify(statusRepository, times(1)).getTransactionDetail("", true)
    }

    @Test
    fun `when getTransactionDetailOffline success statusRepository getTransactionDetailOffline returns success`() {
        `when`(statusRepository.getTransactionDetail("", true)).thenReturn(successTransactionsDetails)
        val result = transactionStatusDetailUseCase.getTransactionDetail("", true)
        assertEquals(successTransactionsDetails, result)
    }

    @Test
    fun `when getTransactionDetailOffline failure statusRepository getTransactionDetailOffline returns fails`() {
        `when`(statusRepository.getTransactionDetail("", true)).thenReturn(error)
        val result = transactionStatusDetailUseCase.getTransactionDetail("", true)
        assertEquals(error, result)
    }

    @Test
    fun `when cancelTransaction success statusRepository cancelTransaction is called`() {
        transactionStatusDetailUseCase.cancelTransaction("")
        verify(statusRepository, times(1)).cancelTransaction("")
    }

    @Test
    fun `when cancelTransaction success statusRepository cancelTransaction returns success`() {
        `when`(statusRepository.cancelTransaction("")).thenReturn(successCancelTransaction)
        val result = transactionStatusDetailUseCase.cancelTransaction("")
        assertEquals(successCancelTransaction, result)
    }

    @Test
    fun `when cancelTransaction failure statusRepository cancelTransaction returns fails`() {
        `when`(statusRepository.cancelTransaction("")).thenReturn(error)
        val result = transactionStatusDetailUseCase.cancelTransaction("")
        assertEquals(error, result)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success statusRepository requestGetPaymentMethodToChangeToBankTransfer is called`() {
        transactionStatusDetailUseCase.requestGetPaymentMethodToChangeToBankTransfer("", "")
        verify(statusRepository, times(1)).requestGetPaymentMethodToChangeToBankTransfer("", "")
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success statusRepository requestGetPaymentMethodToChangeToBankTransfer returns success`() {
        `when`(statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")).thenReturn(successPaymentMethods)
        val result = transactionStatusDetailUseCase.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(successPaymentMethods, result)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer failure statusRepository requestGetPaymentMethodToChangeToBankTransfer returns fails`() {
        `when`(statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")).thenReturn(error)
        val result = transactionStatusDetailUseCase.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(error, result)
    }

    @Test
    fun `when changePayment success statusRepository changePayment is called`() {
        transactionStatusDetailUseCase.changePayment("", "", "", "")
        verify(statusRepository, times(1)).changePayment("", "", "", "")
    }

    @Test
    fun `when changePayment success statusRepository changePayment returns success`() {
        `when`(statusRepository.changePayment("", "", "", "")).thenReturn(successChangePaymentMethod)
        val result = transactionStatusDetailUseCase.changePayment("", "", "", "")
        assertEquals(successChangePaymentMethod, result)
    }

    @Test
    fun `when changePayment failure statusRepository changePayment returns fails`() {
        `when`(statusRepository.changePayment("", "", "", "")).thenReturn(error)
        val result = transactionStatusDetailUseCase.changePayment("", "", "", "")
        assertEquals(error, result)
    }

    @Test
    fun `when getReceipt success statusRepository getReceipt is called`() {
        transactionStatusDetailUseCase.getReceipt(false, "", false)
        verify(statusRepository, times(1)).getReceipt(false, "", false)
    }

    @Test
    fun `when getReceipt success statusRepository getReceipt returns success`() {
        `when`(statusRepository.getReceipt(false, "", false)).thenReturn(successReceipt)
        val result = transactionStatusDetailUseCase.getReceipt(false, "", false)
        assertEquals(successReceipt, result)
    }

    @Test
    fun `when getReceipt failure statusRepository getReceipt returns fails`() {
        `when`(statusRepository.getReceipt(false, "", false)).thenReturn(error)
        val result = transactionStatusDetailUseCase.getReceipt(false, "", false)
        assertEquals(error, result)
    }
}
