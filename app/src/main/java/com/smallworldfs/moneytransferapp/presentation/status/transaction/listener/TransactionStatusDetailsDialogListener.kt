package com.smallworldfs.moneytransferapp.presentation.status.transaction.listener

interface TransactionStatusDetailsDialogListener {
    fun dismissChangedPayment()
    fun positiveChangePayment()
    fun dismissChangePayment()
    fun dismissGenericErrorDialog()
    fun dismissSuccessCancellationDialog()
    fun dismissErrorCancellationDialog()
    fun positiveCancel()
    fun dismissCancel()
}
