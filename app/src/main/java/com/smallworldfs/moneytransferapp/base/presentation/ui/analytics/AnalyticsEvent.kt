package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.os.Bundle

interface AnalyticsEvent {

    fun toBundle(): Bundle

    fun getEventType(): String
}
