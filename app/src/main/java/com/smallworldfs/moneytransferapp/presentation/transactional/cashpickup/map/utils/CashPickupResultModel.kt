package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils

import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

data class CashPickupResultModel(
    val representativeName: String = STRING_EMPTY,
    val locationName: String = STRING_EMPTY,
    val locationAddress: String = STRING_EMPTY,
    val fee: Double? = DOUBLE_ZERO,
    val rate: String? = STRING_EMPTY,
    val deliveryTime: String = STRING_EMPTY,
    val locationCode: String = STRING_EMPTY,
    val representativeCode: String = STRING_EMPTY
) : Serializable
