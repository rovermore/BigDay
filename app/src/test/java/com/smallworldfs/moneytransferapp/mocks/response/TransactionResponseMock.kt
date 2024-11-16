package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionsResponse
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TranslatePaymentMethodResponse

object TransactionResponseMock {

    private const val total = 3

    private const val cancellationMessage = "cancellation message"

    private val transactions = arrayListOf<Transaction>().apply {
        add(Transaction())
        add(Transaction())
        add(Transaction())
    }

    val transactionResponse = TransactionsResponse(total, transactions, cancellationMessage)

    val translatedPaymentMethod = TranslatePaymentMethodResponse().apply {
        msg = "message"
        data = hashMapOf(
            Pair("BANKWIRE", "Transferencia Bancaria"),
            Pair("WORLDPAY", "Pago por Tarjeta"),
            Pair("SOFORT", "SOFORT")
        )
    }
}
