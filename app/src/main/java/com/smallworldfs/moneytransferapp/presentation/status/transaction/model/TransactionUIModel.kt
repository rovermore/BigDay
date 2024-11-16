package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_ZERO
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

@Parcelize
data class TransactionStatusUIModel(val cancellable: Boolean = false, val transactions: List<TransactionUIModel> = emptyList()) : Parcelable

@Parcelize
data class TransactionUIModel(
    val id: String = STRING_EMPTY,
    val mtn: String = STRING_EMPTY,
    val status: String = STRING_EMPTY,
    val statusText: String = STRING_EMPTY,
    val statusIcon: Int? = null,
    val paid: Boolean = false,
    val clientId: String = STRING_EMPTY,
    var totalSale: String = STRING_EMPTY,
    val promotionCode: String = STRING_EMPTY,
    val promotionAmount: String = STRING_EMPTY,
    val changeDate: String = STRING_EMPTY,
    val createdAt: Calendar = Calendar.getInstance(),
    val paidDate: String = STRING_EMPTY,
    val updatedAt: String = STRING_EMPTY,
    val paymentType: String = STRING_EMPTY,
    val paymentMethod: String = STRING_EMPTY,
    val paymentUrl: String = STRING_EMPTY,
    val senderCountry: String = STRING_EMPTY,
    val payoutPrincipal: String = STRING_ZERO,
    val beneficiaryFirstName: String = STRING_EMPTY,
    val beneficiaryLastName: String = STRING_EMPTY,
    val beneficiaryCountry: String = STRING_EMPTY,
    val payoutCountry: String = STRING_EMPTY,
    val payoutCurrency: String = STRING_EMPTY,
    val deliveryMethod: String = STRING_EMPTY,
    val deliveryType: String = STRING_EMPTY,
    val bankTitular: String = STRING_EMPTY,
    var bankName: String = STRING_EMPTY,
    val bankName2: String = STRING_EMPTY,
    val fixedFee: String = STRING_EMPTY,
    val variableFee: String = STRING_EMPTY,
    val sendingCurrency: String = STRING_EMPTY,
    val requestDate: String = STRING_EMPTY,
    val cancelTime: String = STRING_EMPTY,
    val beneficiaryCity: String = STRING_EMPTY,
    val beneficiaryBankName: String = STRING_EMPTY,
    val beneficiaryAccountNumber: String = STRING_EMPTY,
    val beneficiaryAddress: String = STRING_EMPTY,
    val beneficiaryZip: String = STRING_EMPTY,
    val beneficiaryMobilePhoneNumber: String = STRING_EMPTY,
    val bankAccountType: String = STRING_EMPTY,
    val payerName: String = STRING_EMPTY,
    val promotionName: String = STRING_EMPTY,
    val statusMsg: String = STRING_EMPTY,
    val bankNumber: String = STRING_EMPTY,
    val bankIban: String = STRING_EMPTY,
    val translatedPaymentMethod: String = STRING_EMPTY,
    val translatedDeliveryMethod: String = STRING_EMPTY,
    val beneficiaryId: String = STRING_EMPTY,
    val rate: Double = 0.0,
    val taxAmount: String = STRING_EMPTY,
    val taxCode: String = STRING_EMPTY,
    val offline: Boolean = false,
    val boleto: Boolean = false,
    var cancellable: Boolean = false,
    val isChallenge: Boolean = false,
    val deliveryInformation: List<TransactionItemValueUIModel> = listOf(),
    val transactionInformation: List<TransactionItemValueUIModel> = listOf(),
    val transactionTaxes: TransactionItemValueUIModel = TransactionItemValueUIModel(STRING_EMPTY, STRING_EMPTY),
    val cancellationMessage: String = STRING_EMPTY,
    val secondaryAction: SecondaryAction = SecondaryAction()

) : Parcelable {

    fun isPaymentMethodChangeable(): Boolean {
        if (secondaryAction.type == SecondaryAction.Type.AWAITING_PAYMENT) {
            if (this.paymentType.equals(Constants.PAYNMENT_METHODS.ONLINEPAYMENT, ignoreCase = true) ||
                this.paymentType.equals(Constants.PAYNMENT_METHODS.SOFORT, ignoreCase = true) ||
                this.paymentType.equals(Constants.PAYNMENT_METHODS.WORLDPAY, ignoreCase = true)
            ) {
                return true
            }
        }
        return false
    }

    fun isNewTransaction(): Boolean {
        return status.equals(Constants.TRANSACTION_STATUS.NEW, ignoreCase = true) || status.equals(Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING, ignoreCase = true)
    }
}

@Parcelize
data class TransactionItemValueUIModel(val title: String, val value: String) : Parcelable

@Parcelize
data class TransactionInfoItemValueUIModel(val titleId: Int, val value: String) : Parcelable

@Parcelize
data class SecondaryAction(val type: Type = Type.EMPTY, val buttonText: String = STRING_EMPTY, val icon: Int? = null) : Parcelable {
    enum class Type {
        EMPTY, AWAITING_BANK_TRANSFER, AWAITING_PAYMENT
    }
}
