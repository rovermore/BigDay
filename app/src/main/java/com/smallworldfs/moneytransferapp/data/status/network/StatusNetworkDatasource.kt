package com.smallworldfs.moneytransferapp.data.status.network

import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.status.domain.model.CancelTransactionResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ChangePaymentMethodResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentMethodsResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionDetailResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionsResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TranslatePaymentMethodResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.CancelTransactionRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ChangePaymentRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PaymentMethodsRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionsRequest
import okhttp3.ResponseBody
import javax.inject.Inject

class StatusNetworkDatasource @Inject constructor(
    private val service: StatusService
) : NetworkDatasource() {

    fun getTranslatedPaymentMethods(): OperationResult<TranslatePaymentMethodResponse, APIError> =
        executeCall(service.getTranslatedPaymentMethods())

    fun getTransactions(request: TransactionsRequest): OperationResult<TransactionsResponse, APIError> =
        executeCall(service.getTransactions(request))

    fun cancelTransaction(request: CancelTransactionRequest): OperationResult<CancelTransactionResponse, APIError> =
        executeCall(service.cancelTransaction(request))

    fun getTransactionDetails(request: TransactionDetailRequest): OperationResult<TransactionDetailResponse, APIError> =
        executeCall(service.getTransactionDetail(request))

    fun getTransactionDetailOffline(request: TransactionDetailRequest): OperationResult<TransactionDetailResponse, APIError> =
        executeCall(service.getOfflineTransactionDetail(request))

    fun requestGetPaymentMethodToChangeToBankTransfer(request: PaymentMethodsRequest): OperationResult<PaymentMethodsResponse, APIError> =
        executeCall(service.getPaymentMethod(request))

    fun changePayment(request: ChangePaymentRequest): OperationResult<ChangePaymentMethodResponse, APIError> =
        executeCall(service.changePayment(request))

    fun getReceipt(request: ReceiptRequest): OperationResult<ResponseBody, APIError> =
        executeCall(service.getReceipt(request))
}
