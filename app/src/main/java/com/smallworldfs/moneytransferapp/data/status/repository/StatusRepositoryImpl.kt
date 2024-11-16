package com.smallworldfs.moneytransferapp.data.status.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.ReceiptPDFMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.TransactionDTOMapper
import com.smallworldfs.moneytransferapp.data.status.mappers.TransactionDetailsDTOMapper
import com.smallworldfs.moneytransferapp.data.status.network.StatusNetworkDatasource
import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.StatusTransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDetailsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.CancelTransactionRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ChangePaymentRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PaymentMethodsRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionsRequest
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData
import java.io.File
import javax.inject.Inject

class StatusRepositoryImpl @Inject constructor(
    private val statusNetworkDatasource: StatusNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val transactionDTOMapper: TransactionDTOMapper,
    private val transactionDetailsDTOMapper: TransactionDetailsDTOMapper,
    private val receiptPDFMapper: ReceiptPDFMapper,
    private val userDataLocalDatasource: UserDataLocalDatasource,
) : StatusRepository {

    override fun getTransactions(): OperationResult<StatusTransactionDTO, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return statusNetworkDatasource.getTransactions(
                    TransactionsRequest(
                        user.userToken,
                        user.id,
                    ),
                ).map {
                    return Success(StatusTransactionDTO(false, transactionDTOMapper.map(it.transactions), it.total, it.cancellationMessage))
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }

    override fun getTranslatePaymentMethods(): OperationResult<HashMap<String, String>, Error> {
        return statusNetworkDatasource.getTranslatedPaymentMethods()
            .map {
                return Success(it.data)
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun cancelTransaction(mtn: String): OperationResult<String, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return statusNetworkDatasource.cancelTransaction(
                    CancelTransactionRequest(
                        user.id,
                        user.userToken,
                        mtn,
                    ),
                ).map {
                    if (!it.result) {
                        return Failure(Error.OperationCompletedWithError(it.msg))
                    } else {
                        return Success(it.msg)
                    }
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }

    override fun getTransactionDetail(mtn: String, offline: Boolean): OperationResult<TransactionDetailsDTO, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                if (!offline) {
                    return statusNetworkDatasource.getTransactionDetails(
                        TransactionDetailRequest(
                            user.userToken,
                            user.id,
                            mtn,
                        ),
                    ).map {
                        return Success(transactionDetailsDTOMapper.map(it))
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
                } else {
                    return statusNetworkDatasource.getTransactionDetailOffline(
                        TransactionDetailRequest(
                            user.userToken,
                            user.id,
                            mtn,
                        ),
                    ).map {
                        return Success(transactionDetailsDTOMapper.map(it))
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }

    override fun requestGetPaymentMethodToChangeToBankTransfer(country: String, paymentMethod: String): OperationResult<FormData, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return statusNetworkDatasource.requestGetPaymentMethodToChangeToBankTransfer(
                    PaymentMethodsRequest(
                        country,
                        paymentMethod,
                        user.id,
                        user.userToken,
                    ),
                ).map {
                    return Success(it.inputs)
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }

    override fun changePayment(paymentMethod: String, mtn: String, depositBankBranchId: String, depositBankId: String): OperationResult<String, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return statusNetworkDatasource.changePayment(
                    ChangePaymentRequest(
                        paymentMethod,
                        user.id,
                        user.userToken,
                        mtn,
                        depositBankBranchId,
                        depositBankId,
                    ),
                ).map {
                    return Success(it.text)
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }

    override fun getReceipt(offline: Boolean, mtn: String, pre: Boolean): OperationResult<File?, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return statusNetworkDatasource.getReceipt(
                    ReceiptRequest(
                        user.id,
                        user.userToken,
                        mtn,
                        offline,
                        pre,
                    ),
                ).map {
                    return Success(receiptPDFMapper.map(it))
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.mapFailure {
                return Failure(Error.UnregisteredUser("User not found"))
            }
    }
}
