package com.smallworldfs.moneytransferapp.utils.widget

import android.app.Activity
import android.app.Application
import android.os.Bundle

object SWMapActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityPostCreated(activity: Activity, bundle: Bundle?) {
        if (activity is MapProvider) {
            activity.getMap()?.onCreate(bundle)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is MapProvider) {
            activity.getMap()?.onStart()
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is MapProvider) {
            activity.getMap()?.onResume()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (activity is MapProvider) {
            activity.getMap()?.onPause()
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (activity is MapProvider) {
            activity.getMap()?.onStop()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        if (activity is MapProvider) {
            activity.getMap()?.onSaveInstanceState(bundle)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is MapProvider) {
            activity.getMap()?.onDestroy()
        }
    }
}
