package com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.model

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryActivityDTO(
    val transactionInfoDTO: TransactionInfoDTO,
    val transactions: List<Transaction>,
    val cancellationMessage: String = STRING_EMPTY
)

data class TransactionInfoDTO(
    var moneySent: Double = DOUBLE_ZERO,
    var moneyReceived: Double = DOUBLE_ZERO,
    var transactionsCount: Double = DOUBLE_ZERO
)
