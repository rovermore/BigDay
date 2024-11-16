package com.smallworldfs.moneytransferapp.domain.migrated.promotions.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class PromotionDTO(
    val promotionName: String = STRING_EMPTY,
    val promotionCode: String = STRING_EMPTY,
    val countryOrigin: String = STRING_EMPTY,
    val countryDestination: String = STRING_EMPTY,
    val discount: String = STRING_EMPTY,
    val expireDate: String = STRING_EMPTY,
    var discountType: String = STRING_EMPTY,
)
