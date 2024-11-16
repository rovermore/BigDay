package com.smallworldfs.moneytransferapp.presentation.transferdetails

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import java.text.NumberFormat
import javax.inject.Inject

class TransferDetailUseCase @Inject constructor(
    private val numberFormat: NumberFormat,
    private val userDataRepository: UserDataRepository
) {

    fun getLoggedUserCountry(): OperationResult<String, Error> =
        userDataRepository.getLoggedUser()
            .map {
                it.country.countries.first().iso3
            }

    fun getTransferDetail(transaction: Transaction): OperationResult<Transaction, Error> =
        if (!transaction.bankIban.isNullOrBlank() && !transaction.totalSale.isNullOrBlank() && !transaction.mtn.isNullOrBlank() && !transaction.bankTitular.isNullOrBlank()) {
            if (!transaction.bankName.isNullOrBlank()) {
                transaction.totalSale = "${formalizeNumber(transaction.totalSale)} " + transaction.sendingCurrency
            } else if (!transaction.bankName2.isNullOrBlank()) {
                transaction.bankName = transaction.bankName2
                transaction.totalSale = "${formalizeNumber(transaction.totalSale)} " + transaction.sendingCurrency
            } else {
                Failure(Error.ConnectionError())
            }
            Success(transaction)
        } else {
            Failure(Error.ConnectionError())
        }

    private fun formalizeNumber(number: String): String {
        val convertedNumber =
            if (number.isEmpty() || number.isBlank()) {
                0.0
            } else {
                try {
                    number.toDouble()
                } catch (e: Exception) {
                    0.0
                }
            }

        numberFormat.maximumFractionDigits = 2
        numberFormat.minimumFractionDigits = 2

        return numberFormat.format(convertedNumber)
    }
}
