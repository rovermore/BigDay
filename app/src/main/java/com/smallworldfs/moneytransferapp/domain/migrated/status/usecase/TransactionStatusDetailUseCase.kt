package com.smallworldfs.moneytransferapp.domain.migrated.status.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDetailsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData
import java.io.File
import javax.inject.Inject

class TransactionStatusDetailUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val statusRepository: StatusRepository
) {

    fun getLoggedUser(): OperationResult<UserDTO, Error> =
        userDataRepository.getLoggedUser()

    fun getTransactionDetail(mtn: String, offline: Boolean): OperationResult<TransactionDetailsDTO, Error> =
        statusRepository.getTransactionDetail(mtn, offline)

    fun cancelTransaction(mtn: String): OperationResult<String, Error> =
        statusRepository.cancelTransaction(mtn)

    fun requestGetPaymentMethodToChangeToBankTransfer(country: String, paymentMethod: String): OperationResult<FormData, Error> =
        statusRepository.requestGetPaymentMethodToChangeToBankTransfer(country, paymentMethod)

    fun changePayment(paymentMethod: String, mtn: String, depositBankBranchId: String, depositBankId: String): OperationResult<String, Error> =
        statusRepository.changePayment(paymentMethod, mtn, depositBankBranchId, depositBankId)

    fun getReceipt(offline: Boolean, mtn: String, pre: Boolean): OperationResult<File?, Error> =
        statusRepository.getReceipt(offline, mtn, pre)
}
