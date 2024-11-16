package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import javax.inject.Inject

class AnalyticsFragmentLifecycleCallback @Inject constructor(
    private val analyticsSender: AnalyticsSender
) : FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        if (f is GenericFragment) {
            analyticsSender.trackScreen(f.javaClass.simpleName)
        }
    }
}
