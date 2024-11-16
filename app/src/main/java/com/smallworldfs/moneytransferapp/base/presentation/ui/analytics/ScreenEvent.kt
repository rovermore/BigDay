package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.os.Bundle
import com.smallworldfs.moneytransferapp.BuildConfig
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class ScreenEvent(
    val screenName: String = STRING_EMPTY,
    val screenCategory: String = STRING_EMPTY,
    val hierarchy: String = screenName.plus("_").plus(screenCategory),
    val envType: String = BuildConfig.FLAVOR,
    val checkoutStep: String = STRING_EMPTY
) : AnalyticsEvent {

    override fun toBundle() = Bundle().apply {
        putString("screenName", screenName)
        putString("hierarchy", hierarchy)
        putString("envType", envType)
        putString("screenCategory", screenCategory)
        putString("checkoutStep", checkoutStep)
    }

    override fun getEventType(): String {
        return "fbView"
    }
}
