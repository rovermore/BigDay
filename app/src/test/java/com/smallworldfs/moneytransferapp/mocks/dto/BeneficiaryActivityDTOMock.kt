package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.BeneficiaryActivityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model.TransactionInfoDTO
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction

object BeneficiaryActivityDTOMock {

    private val transactionInfoDTO = TransactionInfoDTO(
        moneySent = 120.30,
        moneyReceived = 115.89,
        transactionsCount = 3.89
    )

    private val transactions = listOf(
        Transaction(),
        Transaction(),
        Transaction()
    )

    val beneficiaryActivityDTO = BeneficiaryActivityDTO(
        transactionInfoDTO,
        transactions,
        "Cancellation message",
    )
}
