package com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class RequestCashPickUpChooseLocationDataModel(
    val userId: String = STRING_EMPTY,
    val userToken: String = STRING_EMPTY,
    val deliveryMethod: String = STRING_EMPTY,
    val locationCode: String = STRING_EMPTY,
    val representativeCode: String = STRING_EMPTY,
    val beneficiaryId: String = STRING_EMPTY,
    val pickUpFee: String = STRING_EMPTY,
    val pickUpRate: String = STRING_EMPTY
)
