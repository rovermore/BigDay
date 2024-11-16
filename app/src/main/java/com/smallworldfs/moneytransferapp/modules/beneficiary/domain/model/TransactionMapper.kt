package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model

import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionMapper {

    fun map(transactionUIModel: TransactionUIModel): Transaction {
        with(transactionUIModel) {
            return Transaction().buildInstance(
                id,
                mtn,
                status,
                if (paid) "1" else "2",
                clientId,
                senderCountry,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                totalSale,
                promotionCode,
                changeDate,
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(createdAt.time),
                updatedAt,
                paymentType,
                paymentMethod,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                paymentUrl,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                payoutPrincipal,
                beneficiaryFirstName,
                STRING_EMPTY,
                beneficiaryCountry,
                payoutCurrency,
                bankTitular,
                bankName,
                bankName2,
                bankNumber,
                bankIban,
                fixedFee,
                variableFee,
                sendingCurrency,
                beneficiaryCity,
                beneficiaryBankName,
                translatedPaymentMethod,
                translatedDeliveryMethod,
                beneficiaryId,
                taxAmount,
                taxCode,
                paidDate,
                requestDate,
                cancelTime,
                promotionAmount,
                offline,
                boleto,
                isChallenge,
                TransactionItemValue(transactionTaxes.title, transactionTaxes.value),
                STRING_EMPTY,
                bankAccountType
            )
        }
    }
}
