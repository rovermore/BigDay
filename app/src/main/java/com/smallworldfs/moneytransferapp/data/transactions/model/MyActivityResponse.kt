package com.smallworldfs.moneytransferapp.data.transactions.model

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class MyActivityResponse(
    var msg: String = STRING_EMPTY,
    var transactions: ArrayList<Transaction> = arrayListOf(),
    var transactionsInfo: TransactionsInfo = TransactionsInfo(),
    var cancellationMessage: String = STRING_EMPTY
)

data class TransactionsInfo(
    var moneyReceived: Double = DOUBLE_ZERO,
    var moneySent: Double = DOUBLE_ZERO,
    var transactionsCount: Int = INT_ZERO
)
