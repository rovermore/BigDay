package com.smallworldfs.moneytransferapp.modules.status.domain.service

import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.status.domain.model.AdditionalInfo
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ChangePaymentMethodResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PayNowUrlResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentMethodsResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionDetailResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionsResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ChangePaymentRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PayTransactionRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PaymentMethodsRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TipsRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionsRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import rx.Observable

interface StatusServiceLegacy {

    @GET(EndPoint.GET_TRANSACTIONS)
    fun getTransactions(@QueryMap request: TransactionsRequest?): Observable<Response<TransactionsResponse>?>?

    @GET(EndPoint.PAYMENT_METHODS)
    fun getPaymentMethod(@QueryMap request: PaymentMethodsRequest?): Observable<Response<PaymentMethodsResponse>?>?

    @PUT(EndPoint.CHANGE_PAYMENT)
    fun changePayment(@QueryMap request: ChangePaymentRequest?): Observable<Response<ChangePaymentMethodResponse>?>?

    @GET(EndPoint.GET_TRANSACTION_DETAIL)
    fun getTransactionDetail(@QueryMap request: TransactionDetailRequest?): Observable<Response<TransactionDetailResponse>?>?

    @GET(EndPoint.GET_OFFLINE_TRANSACTION_DETAIL)
    fun getOfflineTransactionDetail(@QueryMap request: TransactionDetailRequest?): Observable<Response<TransactionDetailResponse>?>?

    @Streaming
    @Headers("Accept: application/pdf", "Accept: application/json")
    @GET(EndPoint.SHOW_RECEIPT)
    fun getReceipt(@QueryMap request: ReceiptRequest?): Observable<Response<ResponseBody>?>

    @GET(EndPoint.PAY_TRANSACTION)
    fun payTransaction(@QueryMap request: PayTransactionRequest?): Observable<Response<PayNowUrlResponse>?>?

    @GET(EndPoint.TRANSFER_ADITIONAL_INFO)
    fun getAdditionalInfoTips(@QueryMap request: TipsRequest?): Observable<Response<AdditionalInfo>?>?
}
