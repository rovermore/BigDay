package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class EcommerceCheckoutInfo(
    val originCountry: String = STRING_EMPTY,
    val destinationCountry: String = STRING_EMPTY,
    val deliveryMethod: String = STRING_EMPTY,
    val itemPrice: String = STRING_EMPTY,
    val itemCurrency: String = STRING_EMPTY,
    val destinationCurrency: String = STRING_EMPTY,
    val coupon: String = STRING_EMPTY
)
