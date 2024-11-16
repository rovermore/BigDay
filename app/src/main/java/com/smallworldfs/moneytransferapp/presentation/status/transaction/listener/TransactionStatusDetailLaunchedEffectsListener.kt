package com.smallworldfs.moneytransferapp.presentation.status.transaction.listener

import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank

interface TransactionStatusDetailLaunchedEffectsListener {
    fun timerExpired()
    fun requestPaymentMethodsLauncher(bank: Bank)
    fun changedPaymentMethod(changedPaymentMethod: String)
    fun pdfReceivedError()
    fun successTransactionCancelled()
}
