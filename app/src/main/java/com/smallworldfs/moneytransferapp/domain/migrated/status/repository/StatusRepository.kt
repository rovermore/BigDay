package com.smallworldfs.moneytransferapp.domain.migrated.status.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.StatusTransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDetailsDTO
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData
import java.io.File

interface StatusRepository {
    fun getTransactionDetail(mtn: String, offline: Boolean): OperationResult<TransactionDetailsDTO, Error>
    fun requestGetPaymentMethodToChangeToBankTransfer(country: String, paymentMethod: String): OperationResult<FormData, Error>
    fun changePayment(paymentMethod: String, mtn: String, depositBankBranchId: String, depositBankId: String): OperationResult<String, Error>
    fun getReceipt(offline: Boolean, mtn: String, pre: Boolean): OperationResult<File?, Error>
    fun getTransactions(): OperationResult<StatusTransactionDTO, Error>
    fun getTranslatePaymentMethods(): OperationResult<HashMap<String, String>, Error>
    fun cancelTransaction(mtn: String): OperationResult<String, Error>
}
