package com.smallworldfs.moneytransferapp.modules.common.presentation.ui

import android.app.Dialog
import android.content.Context
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.app
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenEvent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.components.SingletonComponent

open class GenericDialog(context: Context) : Dialog(context) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DaggerHiltEntryPoint {
        fun analyticsHandler(): AnalyticsSender
    }

    var analyticsSender: AnalyticsSender
    var category: String = ""

    init {
        val hiltEntryPoint = fromApplication(
            app,
            DaggerHiltEntryPoint::class.java,
        )
        analyticsSender = hiltEntryPoint.analyticsHandler()
    }

    var analyticsTag: String = context.javaClass.simpleName

    override fun show() {
        category = getCategory(analyticsTag ?: "")
        analyticsSender.trackDialog(analyticsTag, category)
        super.show()
    }
    fun trackDialog(screenName: String?) {
        category = getCategory(screenName ?: "")
        analyticsSender.trackDialog(screenName ?: "", category)
    }

    fun trackEvent(analyticsEvent: AnalyticsEvent?) {
        analyticsSender.trackEvent(analyticsEvent!!)
    }

    private fun getCategory(screenName: String): String {
        val screenEvent: ScreenEvent = if (screenName.isEmpty()) analyticsSender.getScreenEventProperties(this.javaClass.simpleName) else analyticsSender.getScreenEventProperties(screenName)
        return screenEvent.screenCategory
    }
}
