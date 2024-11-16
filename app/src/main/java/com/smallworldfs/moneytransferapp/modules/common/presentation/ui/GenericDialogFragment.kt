package com.smallworldfs.moneytransferapp.modules.common.presentation.ui

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsActivityLifecycleCallback
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class GenericDialogFragment : DialogFragment() {
    @Inject
    lateinit var analyticsSender: AnalyticsSender

    @Inject
    lateinit var analyticsActivityLifecycleCallback: AnalyticsActivityLifecycleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.application?.registerActivityLifecycleCallbacks(analyticsActivityLifecycleCallback)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        activity?.application?.unregisterActivityLifecycleCallbacks(analyticsActivityLifecycleCallback)
        super.onDestroy()
    }

    fun trackScreen(screenName: String) {
        analyticsSender.trackScreen(screenName)
    }

    fun trackEvent(analyticsEvent: AnalyticsEvent) {
        analyticsSender.trackEvent(analyticsEvent)
    }

    fun getHierarchy(screenName: String): String {
        val screenEvent = if (screenName.isEmpty()) {
            analyticsSender.getScreenEventProperties(this.javaClass.simpleName)
        } else {
            analyticsSender.getScreenEventProperties(screenName)
        }
        return screenEvent.hierarchy
    }
}
