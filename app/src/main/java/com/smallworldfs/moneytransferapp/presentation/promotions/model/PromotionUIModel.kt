package com.smallworldfs.moneytransferapp.presentation.promotions.model

import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class PromotionUIModel(
    val name: String = STRING_EMPTY,
    val origin: String = STRING_EMPTY,
    val destination: String = STRING_EMPTY,
    val expireDate: String = STRING_EMPTY,
    val code: String = STRING_EMPTY,
    val daysRemaining: Int = INT_ZERO,
    val discount: String = STRING_EMPTY,
    val discountType: String = STRING_EMPTY,
    val currency: String = STRING_EMPTY,
    val selected: Boolean = false
)
