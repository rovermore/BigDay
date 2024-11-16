package com.smallworldfs.moneytransferapp.modules.status.domain.service

import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.app
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
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
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import rx.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class StatusNetworkDatasourceLegacy @Inject constructor(
    private val service: StatusServiceLegacy
) : NetworkDatasource() {

    fun getTransactions(request: TransactionsRequest): Observable<Response<TransactionsResponse>> =
        executeCall(service.getTransactions(request))

    fun getTransactionDetail(request: TransactionDetailRequest): Observable<Response<TransactionDetailResponse>> =
        executeCall(service.getTransactionDetail(request))

    fun getOfflineTransactionDetail(request: TransactionDetailRequest): Observable<Response<TransactionDetailResponse>> =
        executeCall(service.getOfflineTransactionDetail(request))

    fun getPaymentMethod(request: PaymentMethodsRequest): Observable<Response<PaymentMethodsResponse>> =
        executeCall(service.getPaymentMethod(request))

    fun changePaymentMethod(request: ChangePaymentRequest): Observable<Response<ChangePaymentMethodResponse>> =
        executeCall(service.changePayment(request))

    fun getReceipt(request: ReceiptRequest?): Observable<File> {
        return service.getReceipt(request).map {
            buildPdfReceipt(it?.body())
        }
    }

    fun payTransaction(request: PayTransactionRequest): Observable<Response<PayNowUrlResponse>> =
        executeCall(service.payTransaction(request))

    fun getTips(request: TipsRequest): Observable<Response<AdditionalInfo>> =
        executeCall(service.getAdditionalInfoTips(request))

    private fun buildPdfReceipt(body: ResponseBody?): File? {
        return if (body == null) {
            null
        } else try {
            val pdfReceipt = File(app.getExternalFilesDir("application/pdf").toString() + File.separator + Constants.CONFIGURATION.RECEIPTS_NAME)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(pdfReceipt)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                }
                outputStream.flush()
                pdfReceipt
            } catch (e: Exception) {
                null
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: Exception) {
            null
        }
    }
}
