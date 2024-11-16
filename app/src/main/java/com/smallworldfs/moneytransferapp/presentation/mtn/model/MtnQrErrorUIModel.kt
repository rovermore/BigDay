package com.smallworldfs.moneytransferapp.presentation.mtn.model

import com.smallworldfs.moneytransferapp.presentation.base.ErrorType

data class MtnQrErrorUIModel(
    val error: ErrorType,
    val country: String,
    val mtn: String,
)
