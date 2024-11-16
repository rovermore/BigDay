package com.smallworldfs.moneytransferapp.data.status.mappers

import com.smallworldfs.moneytransferapp.domain.migrated.status.model.BeneficiaryInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.PaymentInfoDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.status.model.TransactionItemValueDTO
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class TransactionDTOMapper @Inject constructor() {

    fun map(responseList: ArrayList<Transaction>): List<TransactionDTO> {
        val result = mutableListOf<TransactionDTO>()
        responseList.map {
            result.add(map(it))
        }
        return result
    }

    fun map(transaction: Transaction): TransactionDTO =
        TransactionDTO(
            id = transaction.id ?: STRING_EMPTY,
            mtn = transaction.mtn ?: STRING_EMPTY,
            status = transaction.status ?: STRING_EMPTY,
            paid = transaction.paid ?: STRING_EMPTY,
            clientId = transaction.clientId ?: STRING_EMPTY,
            senderCountry = transaction.senderCountry ?: STRING_EMPTY,
            originalId = transaction.originalId ?: STRING_EMPTY,
            clientRelationId = transaction.clientRelationId ?: STRING_EMPTY,
            originalRelationId = transaction.originalRelationId ?: STRING_EMPTY,
            totalSale = transaction.totalSale ?: STRING_EMPTY,
            promotionCode = transaction.promotionCode ?: STRING_EMPTY,
            promotionAmount = transaction.promotionAmount ?: STRING_EMPTY,
            changeDate = transaction.changeDate ?: STRING_EMPTY,
            createdAt = transaction.createdAt ?: STRING_EMPTY,
            updatedAt = transaction.updatedAt ?: STRING_EMPTY,
            pspReference = transaction.pspReference ?: STRING_EMPTY,
            mtnProgram = transaction.mtnProgram ?: STRING_EMPTY,
            ip = transaction.ip ?: STRING_EMPTY,
            confirmOnline = transaction.confirmOnline ?: STRING_EMPTY,
            acknowledge = transaction.acknowledge ?: STRING_EMPTY,
            ioConfCode = transaction.ioConfCode ?: STRING_EMPTY,
            ioInstName = transaction.ioInstName ?: STRING_EMPTY,
            ioText = transaction.ioText ?: STRING_EMPTY,
            oneSweepSend = transaction.oneSweepSend ?: STRING_EMPTY,
            clientName = transaction.clientName ?: STRING_EMPTY,
            timestamp = transaction.timestamp ?: STRING_EMPTY,
            extraCost = transaction.extraCost ?: STRING_EMPTY,
            ec = transaction.ec ?: STRING_EMPTY,
            issuer = transaction.issuer ?: STRING_EMPTY,
            deliveryMethod = transaction.deliveryMethod ?: STRING_EMPTY,
            deliveryType = transaction.deliveryMethod ?: STRING_EMPTY,
            bankTitular = transaction.bankTitular ?: STRING_EMPTY,
            bankName = transaction.bankName ?: STRING_EMPTY,
            bankName2 = transaction.bankName2 ?: STRING_EMPTY,
            fixedFee = transaction.fixedFee ?: STRING_EMPTY,
            variableFee = transaction.variableFee ?: STRING_EMPTY,
            sendingCurrency = transaction.sendingCurrency ?: STRING_EMPTY,
            requestDate = transaction.requestDate ?: STRING_EMPTY,
            cancelTime = transaction.cancelTime ?: STRING_EMPTY,
            promotionName = transaction.promotionName ?: STRING_EMPTY,
            statusMsg = transaction.statusMsg ?: STRING_EMPTY,
            translatedDeliveryMethod = transaction.translatedDeliveryMethod ?: STRING_EMPTY,
            beneficiaryId = transaction.beneficiaryId ?: STRING_EMPTY,
            rate = transaction.rate,
            taxAmount = transaction.taxAmount ?: STRING_EMPTY,
            taxCode = transaction.taxCode ?: STRING_EMPTY,
            offline = transaction.isOffline,
            boleto = transaction.isBoleto,
            cancellable = transaction.isCanCanceled,
            isChallenge = transaction.isChallenge,
            deliveryInformation = itemValueDTOListMapper(transaction.deliveryInformation) ?: listOf(),
            transactionInformation = itemValueDTOListMapper(transaction.transactionInformation) ?: listOf(),
            transactionTaxes = transactionItemValueDTOMapper(transaction.transactionTaxes),
            beneficiaryInfo = mapBeneficiaryInfo(transaction),
            paymentInfo = mapPaymentInfo(transaction)
        )

    private fun mapBeneficiaryInfo(transaction: Transaction) = BeneficiaryInfoDTO(
        beneficiaryFirstName = transaction.beneficiaryFirstName ?: STRING_EMPTY,
        beneficiaryFirstLastName = transaction.beneficiaryFirstLastName ?: STRING_EMPTY,
        beneficiaryCountry = transaction.beneficiaryCountry ?: STRING_EMPTY,
        beneficiaryCity = transaction.beneficiaryCity ?: STRING_EMPTY,
        beneficiaryBankName = transaction.beneficiaryBankName ?: STRING_EMPTY,
        beneficiaryAccountNumber = transaction.beneficiaryAccountNumber ?: STRING_EMPTY,
        beneficiaryAddress = transaction.beneficiaryAddress ?: STRING_EMPTY,
        beneficiaryZip = transaction.beneficiaryZip ?: STRING_EMPTY,
        beneficiaryMobilePhoneNumber = transaction.beneficiaryMobilePhoneNumber ?: STRING_EMPTY,
    )

    private fun mapPaymentInfo(transaction: Transaction) = PaymentInfoDTO(
        paidDate = transaction.paidDate ?: STRING_EMPTY,
        payerName = transaction.payerName ?: STRING_EMPTY,
        paymentType = transaction.paymentType ?: STRING_EMPTY,
        paymentMethod = transaction.paymentMethod ?: STRING_EMPTY,
        payoutPrincipal = transaction.payoutPrincipal ?: STRING_EMPTY,
        routingNumber = transaction.routingNumber ?: STRING_EMPTY,
        paymentUrl = transaction.paymentUrl ?: STRING_EMPTY,
        payoutCountry = transaction.payoutPrincipal ?: STRING_EMPTY,
        payoutCurrency = transaction.payoutCurrency ?: STRING_EMPTY,
        accountNumber = transaction.accountNumber ?: STRING_EMPTY,
        accountType = transaction.accountType ?: STRING_EMPTY,
        iban = transaction.iban ?: STRING_EMPTY,
        bic = transaction.bic ?: STRING_EMPTY,
        bankAccountType = transaction.bankAccountType ?: STRING_EMPTY,
        bankNumber = transaction.bankNumber ?: STRING_EMPTY,
        bankIban = transaction.bankIban ?: STRING_EMPTY,
        translatedPaymentMethod = transaction.paymentMethod ?: STRING_EMPTY,
    )

    private fun itemValueDTOListMapper(list: ArrayList<TransactionItemValue?>?): List<TransactionItemValueDTO>? {
        val resultList = mutableListOf<TransactionItemValueDTO>()
        list?.map {
            resultList.add(transactionItemValueDTOMapper(it))
        } ?: return null
        return resultList
    }

    private fun transactionItemValueDTOMapper(item: TransactionItemValue?): TransactionItemValueDTO =
        TransactionItemValueDTO(item?.title ?: STRING_EMPTY, item?.getValue() ?: STRING_EMPTY)
}
