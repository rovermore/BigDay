package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.os.Bundle
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class UserActionEvent(
    private val eventCategory: String = STRING_EMPTY,
    private val eventAction: String = STRING_EMPTY,
    private val eventLabel: String = STRING_EMPTY,
    private val hierarchy: String = STRING_EMPTY,
    private val formType: String = STRING_EMPTY,
    private val processCategory: String = STRING_EMPTY,
    private val checkoutStep: String = STRING_EMPTY,
    private val origin: String = STRING_EMPTY,
    private val destination: String = STRING_EMPTY,
    private val addressType: String = STRING_EMPTY,
) : AnalyticsEvent {

    override fun toBundle(): Bundle {
        val bundle = Bundle()
        with(bundle) {
            putString("eventCategory", eventCategory)
            putString("eventAction", eventAction)
            putString("eventLabel", eventLabel)
            putString("hierarchy", hierarchy)
            putString("formType", formType)
            putString("processCategory", processCategory)
            putString("checkoutStep", checkoutStep)
            putString("origin", origin)
            putString("destination", destination)
            putString("addressType", addressType)
        }
        return bundle
    }

    override fun getEventType() = "fbEvent"
}
