package com.smallworldfs.moneytransferapp.data.status.network

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.QueryMap
import retrofit2.http.Streaming

interface StatusService {
    @GET(EndPoint.GET_TRANSACTIONS)
    fun getTransactions(@QueryMap request: TransactionsRequest): Call<TransactionsResponse>

    @POST(EndPoint.CANCEL_TRANSACTION)
    fun cancelTransaction(@QueryMap request: CancelTransactionRequest): Call<CancelTransactionResponse>

    @GET(EndPoint.GET_TRANSACTION_DETAIL)
    fun getTransactionDetail(@QueryMap request: TransactionDetailRequest): Call<TransactionDetailResponse>

    @GET(EndPoint.PAYMENT_METHODS)
    fun getPaymentMethod(@QueryMap request: PaymentMethodsRequest): Call<PaymentMethodsResponse>

    @PUT(EndPoint.CHANGE_PAYMENT)
    fun changePayment(@QueryMap request: ChangePaymentRequest): Call<ChangePaymentMethodResponse>

    @GET(EndPoint.GET_OFFLINE_TRANSACTION_DETAIL)
    fun getOfflineTransactionDetail(@QueryMap request: TransactionDetailRequest): Call<TransactionDetailResponse>

    @Streaming
    @Headers("Accept: application/pdf", "Accept: application/json")
    @GET(EndPoint.SHOW_RECEIPT)
    fun getReceipt(@QueryMap request: ReceiptRequest): Call<ResponseBody>

    @GET(EndPoint.PAYMENT_KEYS_METHODS)
    fun getTranslatedPaymentMethods(): Call<TranslatePaymentMethodResponse>
}
