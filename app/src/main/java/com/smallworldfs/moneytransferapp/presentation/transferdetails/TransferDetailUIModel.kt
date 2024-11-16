package com.smallworldfs.moneytransferapp.presentation.transferdetails

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class TransferDetailUIModel(
    val details: Transaction = Transaction(),
    val country: String = STRING_EMPTY
)
