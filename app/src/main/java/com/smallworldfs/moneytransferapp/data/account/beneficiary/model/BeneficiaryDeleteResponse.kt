package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryDeleteResponse(
    var msg: String = STRING_EMPTY,
    var transactionsInfo: TransactionsInfo = TransactionsInfo(),
    var transactions: ArrayList<Transaction> = arrayListOf()
)

data class TransactionsInfo(
    var moneySent: Double = DOUBLE_ZERO,
    var moneyReceived: Double = DOUBLE_ZERO,
    var transactionsCount: Double = DOUBLE_ZERO
)
