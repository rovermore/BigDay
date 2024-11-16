package com.smallworldfs.moneytransferapp.presentation.myactivity.model

import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class TransactionHistoryBarUIModel(
    val transactions: LinkedHashMap<String, MonthlyTransactionHistoryDetail> = LinkedHashMap(),
    val monthlyMax: Double = DOUBLE_ZERO
)

data class MonthlyTransactionHistoryDetail(
    val month: String = STRING_EMPTY,
    val monthlyTotal: Double = DOUBLE_ZERO,
    val transactionsCount: Int = INT_ZERO,
    val monthName: String = STRING_EMPTY,
    val sendingCurrency: String = STRING_EMPTY
)
