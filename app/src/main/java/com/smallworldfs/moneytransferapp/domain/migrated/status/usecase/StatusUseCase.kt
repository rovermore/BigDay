package com.smallworldfs.moneytransferapp.domain.migrated.status.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.StatusTransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.repository.StatusRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject

class StatusUseCase @Inject constructor(
    private val statusRepository: StatusRepository,
    private val userDataRepository: UserDataRepository,
    private val calculatorRepository: CalculatorRepository
) {

    fun getTransactions(): OperationResult<StatusTransactionDTO, Error> {
        return getUser().map { user ->
            val cancellable = user.country.countries.first().iso3 == Constants.COUNTRY.US_COUNTRY_VALUE
            return statusRepository.getTransactions().map { status ->
                return statusRepository.getTranslatePaymentMethods()
                    .map { paymentMethodsTranslated ->
                        return calculatorRepository.getDeliveryMethods().map { deliveryMethodsTranslated ->
                            val mappedTransactionList = mutableListOf<TransactionDTO>()
                            status.transactions.map { transaction ->
                                val translatedPaymentMethod = paymentMethodsTranslated[transaction.paymentInfo.paymentType]
                                translatedPaymentMethod?.let { transaction.paymentInfo.paymentMethod = it }

                                val translatedDeliveryMethod = deliveryMethodsTranslated.find { it.code == transaction.deliveryMethod }
                                translatedDeliveryMethod?.let { transaction.translatedDeliveryMethod = it.translation }

                                mappedTransactionList.add(transaction)
                            }
                            return Success(StatusTransactionDTO(cancellable, mappedTransactionList, status.total, status.cancellationMessage))
                        }.mapFailure {
                            return Failure(it)
                        }
                    }.mapFailure {
                        return Failure(it)
                    }
            }
        }
    }

    fun cancelTransaction(mtn: String): OperationResult<String, Error> =
        statusRepository.cancelTransaction(mtn)

    fun getUser(): OperationResult<UserDTO, Error> =
        userDataRepository.getLoggedUser()
}
