package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.smallworldfs.moneytransferapp.base.presentation.ui.BaseAppCompatActivity
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import javax.inject.Inject

class AnalyticsActivityLifecycleCallback @Inject constructor(
    private val analyticsSender: AnalyticsSender
) : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is GenericActivity || activity is BaseAppCompatActivity<*, *, *>) {
            analyticsSender.trackScreen(activity.javaClass.simpleName)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
