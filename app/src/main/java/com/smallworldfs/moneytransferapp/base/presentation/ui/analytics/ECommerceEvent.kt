package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_SLASH

data class ECommerceEvent(
    private val mtn: String = STRING_EMPTY,
    private val origin: String = STRING_EMPTY,
    private val destination: String = STRING_EMPTY,
    private val itemBrand: String = STRING_EMPTY,
    private val itemPrice: String = STRING_EMPTY,
    private val itemCurrency: String = STRING_EMPTY,
    private val destinationCurrency: String = STRING_EMPTY,
    private val eventType: String = STRING_EMPTY,
    private val hierarchy: String = STRING_EMPTY,
    private val funnelStep: String = STRING_EMPTY,
    private val coupon: String = STRING_EMPTY,
    private val processCategory: String = STRING_EMPTY,
    private val transferOption: String = STRING_EMPTY,
) : AnalyticsEvent {

    private val itemId: String = origin.plus("_2_").plus(destination)
    private val itemName: String = itemId.plus("_BY_").plus(itemBrand).plus("_IN_").plus(destinationCurrency)
    private val itemCategory: String = origin.plus(STRING_SLASH).plus(destination).plus(STRING_SLASH).plus(itemBrand).plus(STRING_SLASH).plus(destinationCurrency)

    override fun toBundle(): Bundle {
        val itemsBundle = Bundle()
        with(itemsBundle) {
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, itemCategory)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, itemBrand)
        }
        return Bundle().apply {
            putBundle("items", itemsBundle)
            putString(FirebaseAnalytics.Param.PRICE, itemPrice)
            putString(FirebaseAnalytics.Param.CURRENCY, itemCurrency)
            if (transferOption.isNotEmpty()) putString("transferOption$funnelStep", transferOption)
        }
    }

    override fun getEventType() = eventType
}
