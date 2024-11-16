package com.smallworldfs.moneytransferapp.utils.widget.timer.sms

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object SWTimerFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        if (f is SWTimerProvider) {
            f.getSwTimer()?.cancel()
        }
    }
}
