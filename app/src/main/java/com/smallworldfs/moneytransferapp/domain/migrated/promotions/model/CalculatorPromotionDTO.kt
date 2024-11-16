package com.smallworldfs.moneytransferapp.domain.migrated.promotions.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CalculatorPromotionDTO(
    val promotionName: String = STRING_EMPTY,
    val discount: Double = 0.0,
    val promotionNumber: String = STRING_EMPTY,
    val autoAssigned: Boolean = false,
    val promotionType: String = STRING_EMPTY,
    val promotionCode: String = STRING_EMPTY
)
