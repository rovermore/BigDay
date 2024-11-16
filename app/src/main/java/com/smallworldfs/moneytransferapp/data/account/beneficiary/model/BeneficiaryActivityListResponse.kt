package com.smallworldfs.moneytransferapp.data.account.beneficiary.model

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class BeneficiaryActivityListResponse(
    var msg: String = STRING_EMPTY,
    var transactionsInfo: TransactionsInfo = TransactionsInfo(),
    var transactions: ArrayList<Transaction> = arrayListOf(),
    var cancellationMessage: String = STRING_EMPTY
)
