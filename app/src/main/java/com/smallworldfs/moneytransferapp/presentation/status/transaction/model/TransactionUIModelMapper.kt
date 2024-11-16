package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import android.content.Context
import android.text.TextUtils
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionItemValueDTO
import com.smallworldfs.moneytransferapp.utils.AmountFormatter
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class TransactionUIModelMapper @Inject constructor(val context: Context) {

    fun map(cancellationMessage: String, transactions: List<TransactionDTO>): List<TransactionUIModel> {
        val result = mutableListOf<TransactionUIModel>()
        transactions.map {
            result.add(map(cancellationMessage, it))
        }
        return result
    }

    fun mapToTransactionStatusUIModel(cancellationMessage: String = STRING_EMPTY, cancellable: Boolean, transactions: List<TransactionDTO>): TransactionStatusUIModel {
        return TransactionStatusUIModel(cancellable, map(cancellationMessage, transactions))
    }

    fun map(cancellationMessage: String, transaction: TransactionDTO): TransactionUIModel =
        TransactionUIModel(
            id = transaction.id,
            mtn = transaction.mtn,
            status = transaction.status,
            statusText = getTransactionStatusText(transaction),
            statusIcon = getStatusIcon(transaction),
            paid = transaction.paid.equals("1", ignoreCase = true),
            clientId = transaction.clientId,
            senderCountry = transaction.senderCountry,
            totalSale = transaction.totalSale,
            promotionCode = transaction.promotionCode,
            promotionAmount = transaction.promotionAmount,
            changeDate = transaction.changeDate,
            createdAt = getCreationDateCalendar(transaction.createdAt),
            paidDate = transaction.paymentInfo.paidDate,
            updatedAt = transaction.updatedAt,
            paymentType = transaction.paymentInfo.paymentType,
            paymentMethod = transaction.paymentInfo.paymentMethod,
            paymentUrl = transaction.paymentInfo.paymentUrl,
            payoutPrincipal = transaction.paymentInfo.payoutPrincipal,
            beneficiaryFirstName = transaction.beneficiaryInfo.beneficiaryFirstName,
            beneficiaryLastName = transaction.beneficiaryInfo.beneficiaryFirstLastName,
            beneficiaryCountry = transaction.beneficiaryInfo.beneficiaryCountry,
            payoutCountry = transaction.beneficiaryInfo.beneficiaryCountry,
            payoutCurrency = transaction.paymentInfo.payoutCurrency,
            deliveryMethod = transaction.deliveryMethod,
            deliveryType = transaction.deliveryMethod,
            bankTitular = transaction.bankTitular,
            bankName = transaction.bankName,
            bankName2 = transaction.bankName2,
            fixedFee = transaction.fixedFee,
            variableFee = transaction.variableFee,
            sendingCurrency = transaction.sendingCurrency,
            requestDate = transaction.requestDate,
            cancelTime = transaction.cancelTime,
            beneficiaryCity = transaction.beneficiaryInfo.beneficiaryCity,
            beneficiaryBankName = transaction.beneficiaryInfo.beneficiaryBankName,
            beneficiaryAccountNumber = transaction.beneficiaryInfo.beneficiaryAccountNumber,
            beneficiaryAddress = transaction.beneficiaryInfo.beneficiaryAddress,
            beneficiaryZip = transaction.beneficiaryInfo.beneficiaryZip,
            beneficiaryMobilePhoneNumber = transaction.beneficiaryInfo.beneficiaryMobilePhoneNumber,
            bankAccountType = transaction.paymentInfo.bankAccountType,
            payerName = transaction.paymentInfo.payerName,
            promotionName = transaction.promotionName,
            statusMsg = transaction.statusMsg,
            bankNumber = transaction.paymentInfo.bankNumber,
            bankIban = transaction.paymentInfo.bankIban,
            translatedPaymentMethod = transaction.paymentInfo.translatedPaymentMethod,
            translatedDeliveryMethod = transaction.translatedDeliveryMethod,
            beneficiaryId = transaction.beneficiaryId,
            rate = transaction.rate,
            taxAmount = transaction.taxAmount,
            taxCode = transaction.taxCode,
            offline = transaction.offline,
            boleto = transaction.boleto,
            cancellable = transaction.cancellable,
            isChallenge = transaction.isChallenge,
            deliveryInformation = itemValueDTOListMapper(transaction.deliveryInformation),
            transactionInformation = itemValueDTOListMapper(transaction.transactionInformation),
            transactionTaxes = TransactionItemValueUIModel(transaction.transactionTaxes.title, formatRate(transaction.transactionTaxes.value)),
            cancellationMessage = cancellationMessage,
            secondaryAction = SecondaryAction(getSecondaryActionType(transaction), getSecondaryActionButtonText(transaction), getSecondaryActionIcon(transaction)),
        )

    private fun getCreationDateCalendar(dateString: String) = if (!TextUtils.isEmpty(dateString)) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.time = formatter.parse(dateString)
            calendar
        } catch (exception: ParseException) {
            Calendar.getInstance()
        }
    } else Calendar.getInstance()

    private fun formatRate(rate: String): String {
        return try {
            val formattedRate = rate.toDouble()
            AmountFormatter.formatDoubleRateNumber(formattedRate)
        } catch (e: NumberFormatException) {
            rate
        }
    }

    private fun itemValueDTOListMapper(list: List<TransactionItemValueDTO>): List<TransactionItemValueUIModel> {
        val resultList = mutableListOf<TransactionItemValueUIModel>()
        if (list.isNotEmpty()) {
            list.forEach { resultList.add(TransactionItemValueUIModel(it.title, it.value)) }
        }
        return resultList
    }

    private fun getTransactionStatusText(transaction: TransactionDTO): String {
        return when (transaction.status) {
            Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING, Constants.TRANSACTION_STATUS.NEW -> if (transaction.paid.equals("1", ignoreCase = true)) {
                context.getString(R.string.status_transaction_in_progress)
            } else {
                if (transaction.paymentInfo.paymentType.equals(Constants.PAYNMENT_METHODS.BANKWIRE, ignoreCase = true)) {
                    context.getString(R.string.status_transaction_awaiting_bank_transfer)
                } else {
                    context.getString(R.string.status_transaction_awaiting_payment)
                }
            }
            Constants.TRANSACTION_STATUS.UNDER_REVIEW -> context.getString(R.string.status_transaction_under_review)
            Constants.TRANSACTION_STATUS.IN_PROGRESS, Constants.TRANSACTION_STATUS.TO_SUBMIT -> context.getString(R.string.status_transaction_in_progress)
            Constants.TRANSACTION_STATUS.VOID_PENDING, Constants.TRANSACTION_STATUS.USER_CANCEL -> context.getString(R.string.status_transaction_cancellation_under_review)
            Constants.TRANSACTION_STATUS.CLOSED_CANCELLED, Constants.TRANSACTION_STATUS.CLOSED_COMPLIANCE, Constants.TRANSACTION_STATUS.CLOSED_REFUSED, Constants.TRANSACTION_STATUS.CLOSED_DECLINED -> context.getString(R.string.status_transaction_transaction_cancelled)
            Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT -> if (transaction.deliveryMethod.equals(Constants.DELIVERY_METHODS.BANK_DEPOSIT, ignoreCase = true)) {
                context.getString(R.string.status_transaction_completed)
            } else {
                context.getString(R.string.status_transaction_money_collected)
            }
            else -> STRING_EMPTY
        }
    }

    private fun getStatusIcon(transaction: TransactionDTO): Int? {
        return when (transaction.status) {
            Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING, Constants.TRANSACTION_STATUS.NEW -> if (transaction.paid.equals("1", ignoreCase = true)) {
                R.drawable.status_icn_inprogress
            } else {
                R.drawable.status_icn_paymentpending
            }
            Constants.TRANSACTION_STATUS.UNDER_REVIEW -> R.drawable.status_icn_inprogress
            Constants.TRANSACTION_STATUS.VOID_PENDING,
            Constants.TRANSACTION_STATUS.CLOSED_CANCELLED,
            Constants.TRANSACTION_STATUS.CLOSED_COMPLIANCE,
            Constants.TRANSACTION_STATUS.CLOSED_REFUSED,
            Constants.TRANSACTION_STATUS.CLOSED_DECLINED,
            Constants.TRANSACTION_STATUS.USER_CANCEL -> R.drawable.status_icn_cancelled
            Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT -> R.drawable.status_icn_complete
            else -> null
        }
    }

    private fun getSecondaryActionType(transaction: TransactionDTO): SecondaryAction.Type {
        if (TextUtils.isEmpty(transaction.status)) {
            return SecondaryAction.Type.EMPTY
        }
        return if (transaction.status.equals(Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING, ignoreCase = true) ||
            transaction.status.equals(Constants.TRANSACTION_STATUS.NEW, ignoreCase = true)
        ) {
            if (transaction.paid.equals("1", ignoreCase = true)) {
                SecondaryAction.Type.EMPTY
            } else {
                if (transaction.paymentInfo.paymentType.equals(Constants.PAYNMENT_METHODS.BANKWIRE, ignoreCase = true)) {
                    SecondaryAction.Type.AWAITING_BANK_TRANSFER
                } else {
                    SecondaryAction.Type.AWAITING_PAYMENT
                }
            }
        } else {
            SecondaryAction.Type.EMPTY
        }
    }

    private fun getSecondaryActionIcon(transaction: TransactionDTO): Int? {
        return when (getSecondaryActionType(transaction)) {
            SecondaryAction.Type.AWAITING_BANK_TRANSFER -> R.drawable.status_icn_details
            SecondaryAction.Type.AWAITING_PAYMENT -> null
            else -> null
        }
    }

    private fun getSecondaryActionButtonText(transaction: TransactionDTO): String {
        return if (transaction.isChallenge) {
            STRING_EMPTY
        } else {
            if (transaction.cancellable) {
                context.getString(R.string.cancel_transaction_transaction_status_button).uppercase(Locale.getDefault())
            } else {
                if (transaction.boleto) {
                    context.getString(R.string.secondary_button_text_action_print_boleto)
                } else {
                    when (getSecondaryActionType(transaction)) {
                        SecondaryAction.Type.AWAITING_BANK_TRANSFER -> context.getString(R.string.secondary_button_text_action_transfer_details_dialog)
                        SecondaryAction.Type.AWAITING_PAYMENT -> if (transaction.paid.equals("1", ignoreCase = true)) {
                            STRING_EMPTY
                        } else {
                            context.getString(R.string.secondary_button_text_action_pay_now)
                        }
                        else -> STRING_EMPTY
                    }
                }
            }
        }
    }
}
