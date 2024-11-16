package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class PayTransactionUIModel(
    val mtn: String = STRING_EMPTY,
    val url: String = STRING_EMPTY
)
