package com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class RequestCashPickUpLocationsDataModel(
    val userToken: String = STRING_EMPTY,
    val userId: String = STRING_EMPTY,
    val amount: String = STRING_EMPTY,
    val currencyType: String = STRING_EMPTY,
    val currencyOrigin: String = STRING_EMPTY,
    val beneficiaryId: String = STRING_EMPTY
)
