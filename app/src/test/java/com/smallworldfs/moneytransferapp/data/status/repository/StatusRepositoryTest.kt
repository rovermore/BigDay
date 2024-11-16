package com.smallworldfs.moneytransferapp.data.status.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.ReceiptPDFMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.TransactionDTOMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.TransactionDetailsDTOMapper
import com.smallworldfs.moneytransferapp.data.status.network.StatusNetworkDatasource
import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.mocks.FileMock
import com.smallworldfs.moneytransferapp.mocks.FormDataMock
import com.smallworldfs.moneytransferapp.mocks.TranslationsMock
import com.smallworldfs.moneytransferapp.mocks.dto.StatusTransactionDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.TransactionDetailsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.CancelTransactionResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.ChangePaymentMethodResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.PaymentMethodsResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.ResponseBodyMock
import com.smallworldfs.moneytransferapp.mocks.response.TransactionDetailsResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.TransactionResponseMock
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.CancelTransactionRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ChangePaymentRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PaymentMethodsRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionsRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class StatusRepositoryTest {

    @Mock
    lateinit var statusNetworkDatasource: StatusNetworkDatasource

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var transactionDTOMapper: TransactionDTOMapper

    @Mock
    lateinit var transactionDetailsDTOMapper: TransactionDetailsDTOMapper

    @Mock
    lateinit var receiptPDFMapper: ReceiptPDFMapper

    @Mock
    lateinit var userDataLocalDatasource: UserDataLocalDatasource

    private lateinit var statusRepository: StatusRepository

    private val user = UserDTOMock.userDTO
    private val successUser = Success(user)
    private val token = user.userToken
    private val id = user.id

    private val paymentMethodTranslated = TranslationsMock.paymentMethodTranslated
    private val successPaymentMethodTranslated = Success(paymentMethodTranslated)

    private val statusTransactionDTO = StatusTransactionDTOMock.statusTransactionDTO
    private val successStatusTransactionDTO = Success(statusTransactionDTO)

    private val transactionsDTOList = StatusTransactionDTOMock.transactions

    private val transactionResponse = TransactionResponseMock.transactionResponse
    private val successTransactionResponse = Success(transactionResponse)

    private val cancelTransactionSuccessResponse = CancelTransactionResponseMock.cancelTransactionSuccessResponse
    private val successCancelTransactionResponse = Success(cancelTransactionSuccessResponse)
    private val successCancelledTransaction = Success("")

    private val transactionDetailsResponse = TransactionDetailsResponseMock.transactionDetailResponse
    private val successTransactionsDetailsResponse = Success(transactionDetailsResponse)
    private val transactionDetailsDTO = TransactionDetailsDTOMock.transactionDetailsDTO
    private val successTransactionDetailsDTO = Success(transactionDetailsDTO)

    private val inputs = FormDataMock.formData
    private val paymentMethodsResponse = PaymentMethodsResponseMock.paymentMethodResponse
    private val successPaymentMethodsResponse = Success(paymentMethodsResponse)
    private val successPaymentMethods = Success(inputs)

    private val textPaymentMethodResponse = ""
    private val changePaymentMethodResponse = ChangePaymentMethodResponseMock.changePaymentMethodResponse
    private val successChangePaymentMethodsResponse = Success(changePaymentMethodResponse)
    private val successChangePaymentMethods = Success(textPaymentMethodResponse)

    private val responseBody = ResponseBodyMock.emptyResponseBody
    private val successResponseBody = Success(responseBody)
    private val file = FileMock.file
    private val successFile = Success(file)

    private val error = Error.UnregisteredUser("Uncompleted operation")
    private val failureError = Failure(error)
    private val apiError = APIError.UnmappedError(0, "")
    private val failureApiError = Failure(apiError)

    private val paymentMethodResponse = TransactionResponseMock.translatedPaymentMethod
    private val successPaymentMethodResponse = Success(paymentMethodResponse)
    private val mtn = "2342984"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        statusRepository = StatusRepositoryImpl(
            statusNetworkDatasource,
            apiErrorMapper,
            transactionDTOMapper,
            transactionDetailsDTOMapper,
            receiptPDFMapper,
            userDataLocalDatasource
        )
    }

    @After
    fun tearDown() {
        file.delete()
    }

    @Test
    fun `when getTransactions success statusNetworkDatasource getTransactions is called`() {
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(
            statusNetworkDatasource.getTransactions(
                TransactionsRequest(
                    token,
                    id
                )
            )
        ).thenReturn(successTransactionResponse)
        statusRepository.getTransactions()
        verify(statusNetworkDatasource, times(1)).getTransactions(
            TransactionsRequest(
                token,
                id
            )
        )
    }

    @Test
    fun `when getTransactions success statusNetworkDatasource getTransactions returns success`() {
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(
            statusNetworkDatasource.getTransactions(
                TransactionsRequest(
                    token,
                    id
                )
            )
        ).thenReturn(successTransactionResponse)
        `when`(transactionDTOMapper.map(transactionResponse.transactions)).thenReturn(transactionsDTOList)
        val result = statusRepository.getTransactions()
        assertEquals(successStatusTransactionDTO.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactions failure userDataLocalDatasource retrieveUser returns error`() {
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        `when`(
            statusNetworkDatasource.getTransactions(
                TransactionsRequest(
                    token,
                    id
                )
            )
        ).thenReturn(failureApiError)
        val result = statusRepository.getTransactions()
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactions failure statusNetworkDatasource getTransactions returns error`() {
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(
            statusNetworkDatasource.getTransactions(
                TransactionsRequest(
                    token,
                    id
                )
            )
        ).thenReturn(failureApiError)
        `when`(apiErrorMapper.map(apiError)).thenReturn(error)
        val result = statusRepository.getTransactions()
        assertEquals(failureError, result)
    }

    @Test
    fun `when getTranslatePaymentMethods success statusNetworkDatasource getTranslatePaymentMethods is called`() {
        `when`(statusNetworkDatasource.getTranslatedPaymentMethods()).thenReturn(successPaymentMethodResponse)
        statusRepository.getTranslatePaymentMethods()
        verify(statusNetworkDatasource, times(1)).getTranslatedPaymentMethods()
    }

    @Test
    fun `when getTranslatePaymentMethods success statusNetworkDatasource getTranslatePaymentMethods returns success`() {
        `when`(statusNetworkDatasource.getTranslatedPaymentMethods()).thenReturn(successPaymentMethodResponse)
        val result = statusRepository.getTranslatePaymentMethods()
        assertEquals(successPaymentMethodTranslated, result)
    }

    @Test
    fun `when getTranslatePaymentMethods failure statusNetworkDatasource getTranslatePaymentMethods returns error`() {
        `when`(statusNetworkDatasource.getTranslatedPaymentMethods()).thenReturn(failureApiError)
        `when`(apiErrorMapper.map(apiError)).thenReturn(error)
        val result = statusRepository.getTranslatePaymentMethods()
        assertEquals(failureError, result)
    }

    @Test
    fun `when cancelTransaction success userDataLocalDatasource getLoggedUser is called`() {
        `when`(statusNetworkDatasource.cancelTransaction(CancelTransactionRequest(id, token, "")))
            .thenReturn(successCancelTransactionResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.cancelTransaction("")
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when cancelTransaction success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.cancelTransaction(
                CancelTransactionRequest(id, token, "")
            )
        ).thenReturn(successCancelTransactionResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.cancelTransaction("")
        assertEquals(successCancelledTransaction, result)
    }

    @Test
    fun `when cancelTransaction failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.cancelTransaction(
                CancelTransactionRequest(id, token, "")
            )
        ).thenReturn(successCancelTransactionResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.cancelTransaction("")
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when cancelTransaction success statusNetworkDatasource cancelTransaction is called`() {
        `when`(
            statusNetworkDatasource.cancelTransaction(
                CancelTransactionRequest(id, token, "")
            )
        ).thenReturn(successCancelTransactionResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.cancelTransaction("")
        verify(statusNetworkDatasource, times(1)).cancelTransaction(
            CancelTransactionRequest(id, token, "")
        )
    }

    @Test
    fun `when cancelTransaction success statusNetworkDatasource cancelTransaction returns success`() {
        val mtn = "123"
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(
            statusNetworkDatasource.cancelTransaction(
                CancelTransactionRequest(id, token, mtn)
            )
        ).thenReturn(successCancelTransactionResponse)
        val result = statusRepository.cancelTransaction(mtn)
        assertEquals(successCancelledTransaction, result)
    }

    @Test
    fun `when cancelTransaction failure statusNetworkDatasource cancelTransaction returns fails`() {
        val mtn = "123"
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(
            statusNetworkDatasource.cancelTransaction(
                CancelTransactionRequest(id, token, mtn)
            )
        ).thenReturn(failureApiError)
        val result = statusRepository.cancelTransaction(mtn)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactionDetail success userDataLocalDatasource getLoggedUser is called`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getTransactionDetail(mtn, false)
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when getTransactionDetail success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(transactionDetailsDTOMapper.map(transactionDetailsResponse)).thenReturn(transactionDetailsDTO)
        val result = statusRepository.getTransactionDetail(mtn, false)
        assertEquals(successTransactionDetailsDTO, result)
    }

    @Test
    fun `when getTransactionDetail failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.getTransactionDetail("", false)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactionDetail success statusNetworkDatasource getTransactionDetails is called`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getTransactionDetail(mtn, false)
        verify(statusNetworkDatasource, times(1)).getTransactionDetails(
            TransactionDetailRequest(token, id, mtn)
        )
    }

    @Test
    fun `when getTransactionDetail success statusNetworkDatasource getTransactionDetails returns success`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(transactionDetailsDTOMapper.map(transactionDetailsResponse)).thenReturn(transactionDetailsDTO)
        val result = statusRepository.getTransactionDetail(mtn, false)
        assertEquals(successTransactionDetailsDTO, result)
    }

    @Test
    fun `when getTransactionDetail failure statusNetworkDatasource getTransactionDetails returns fails`() {
        `when`(
            statusNetworkDatasource.getTransactionDetails(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(failureApiError)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.getTransactionDetail(mtn, false)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactionDetailOffline success userDataLocalDatasource getLoggedUser is called`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getTransactionDetail(mtn, true)
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when getTransactionDetailOffline success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(transactionDetailsDTOMapper.map(transactionDetailsResponse)).thenReturn(transactionDetailsDTO)
        val result = statusRepository.getTransactionDetail(mtn, true)
        assertEquals(successTransactionDetailsDTO, result)
    }

    @Test
    fun `when getTransactionDetailOffline failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.getTransactionDetail(mtn, true)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getTransactionDetailOffline success statusNetworkDatasource getTransactionDetailOffline is called`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getTransactionDetail(mtn, true)
        verify(statusNetworkDatasource, times(1)).getTransactionDetailOffline(
            TransactionDetailRequest(token, id, mtn)
        )
    }

    @Test
    fun `when getTransactionDetailOffline success statusNetworkDatasource getTransactionDetailOffline returns success`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(successTransactionsDetailsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(transactionDetailsDTOMapper.map(transactionDetailsResponse)).thenReturn(transactionDetailsDTO)
        val result = statusRepository.getTransactionDetail(mtn, true)
        assertEquals(successTransactionDetailsDTO, result)
    }

    @Test
    fun `when getTransactionDetailOffline failure statusNetworkDatasource getTransactionDetailOffline returns fails`() {
        `when`(
            statusNetworkDatasource.getTransactionDetailOffline(
                TransactionDetailRequest(token, id, mtn)
            )
        ).thenReturn(failureApiError)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.getTransactionDetail(mtn, true)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success userDataLocalDatasource getLoggedUser is called`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(successPaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(successPaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(successPaymentMethods, result)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(successPaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success statusNetworkDatasource requestGetPaymentMethodToChangeToBankTransfer is called`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(successPaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        verify(statusNetworkDatasource, times(1)).requestGetPaymentMethodToChangeToBankTransfer(
            PaymentMethodsRequest("", "", id, token)
        )
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer success statusNetworkDatasource requestGetPaymentMethodToChangeToBankTransfer returns success`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(successPaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(successPaymentMethods, result)
    }

    @Test
    fun `when requestGetPaymentMethodToChangeToBankTransfer failure statusNetworkDatasource requestGetPaymentMethodToChangeToBankTransfer returns fails`() {
        `when`(
            statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                PaymentMethodsRequest("", "", id, token)
            )
        ).thenReturn(failureApiError)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.requestGetPaymentMethodToChangeToBankTransfer("", "")
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when changePayment success userDataLocalDatasource getLoggedUser is called`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(successChangePaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.changePayment("", "", "", "")
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when changePayment success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(successChangePaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.changePayment("", "", "", "")
        assertEquals(successChangePaymentMethods, result)
    }

    @Test
    fun `when changePayment failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(successChangePaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.changePayment("", "", "", "")
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when changePayment success statusNetworkDatasource changePayment is called`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(successChangePaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.changePayment("", "", "", "")
        verify(statusNetworkDatasource, times(1)).changePayment(
            ChangePaymentRequest("", id, token, "", "", "")
        )
    }

    @Test
    fun `when changePayment success statusNetworkDatasource changePayment returns success`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(successChangePaymentMethodsResponse)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.changePayment("", "", "", "")
        assertEquals(successChangePaymentMethods, result)
    }

    @Test
    fun `when changePayment failure statusNetworkDatasource changePayment returns fails`() {
        `when`(
            statusNetworkDatasource.changePayment(
                ChangePaymentRequest("", id, token, "", "", "")
            )
        ).thenReturn(failureApiError)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.changePayment("", "", "", "")
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getReceipt success userDataLocalDatasource getLoggedUser is called`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(successResponseBody)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getReceipt(false, "", false)
        verify(userDataLocalDatasource, times(1)).retrieveUser()
    }

    @Test
    fun `when getReceipt success userDataLocalDatasource getLoggedUser returns success`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(successResponseBody)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(receiptPDFMapper.map(responseBody)).thenReturn(file)
        val result = statusRepository.getReceipt(false, "", false)
        assertEquals(successFile, result)
    }

    @Test
    fun `when getReceipt failure userDataLocalDatasource getLoggedUser returns fails`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(successResponseBody)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(failureError)
        val result = statusRepository.getReceipt(false, "", false)
        assertEquals(failureError.javaClass, result.javaClass)
    }

    @Test
    fun `when getReceipt success statusNetworkDatasource getReceipt is called`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(successResponseBody)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        statusRepository.getReceipt(false, "", false)
        verify(statusNetworkDatasource, times(1)).getReceipt(
            ReceiptRequest(id, token, "", false, false)
        )
    }

    @Test
    fun `when getReceipt success statusNetworkDatasource getReceipt returns success`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(successResponseBody)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        `when`(receiptPDFMapper.map(responseBody)).thenReturn(file)
        val result = statusRepository.getReceipt(false, "", false)
        assertEquals(successFile, result)
    }

    @Test
    fun `when getReceipt failure statusNetworkDatasource getReceipt returns fails`() {
        `when`(
            statusNetworkDatasource.getReceipt(
                ReceiptRequest(id, token, "", false, false)
            )
        ).thenReturn(failureApiError)
        `when`(userDataLocalDatasource.retrieveUser()).thenReturn(successUser)
        val result = statusRepository.getReceipt(false, "", false)
        assertEquals(failureError.javaClass, result.javaClass)
    }
}
