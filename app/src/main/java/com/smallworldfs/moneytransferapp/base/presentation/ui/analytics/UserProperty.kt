package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.os.Bundle

data class UserProperty(
    val name: UserPropertyName,
    val value: String
) : AnalyticsEvent {

    override fun toBundle(): Bundle {
        val bundle = Bundle()
        with(bundle) {
            putString("name", name.value)
            putString("value", value)
        }
        return bundle
    }

    override fun getEventType() = "fbEvent"
}

enum class UserPropertyName(val value: String) {
    ORIGIN("origin"),
    DESTINATION("destination"),
    USER_ID("userId")
}
