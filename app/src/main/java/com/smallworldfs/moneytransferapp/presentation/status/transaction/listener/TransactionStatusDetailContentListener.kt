package com.smallworldfs.moneytransferapp.presentation.status.transaction.listener

import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel

interface TransactionStatusDetailContentListener {
    fun onContactSupportClick()
    fun onShowPreReceiptClick()
    fun onShowReceiptClick()
    fun onCancelButtonClick()
    fun onShowDetailsClick(transactionUIModel: TransactionUIModel)
    fun onRightButtonClick()
    fun onPayNowClick()
    fun onLeftButtonClick()
    fun onTopErrorClick()
    fun genericErrorRetryAction()
}
