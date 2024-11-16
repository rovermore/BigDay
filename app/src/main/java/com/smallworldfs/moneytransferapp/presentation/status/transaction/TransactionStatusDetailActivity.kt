package com.smallworldfs.moneytransferapp.presentation.status.transaction

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerActivityLifecycleCallback
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionStatusDetailActivity : GenericActivity(), SWTimerProvider {

    companion object {
        const val TAG = "TransactionStatusDetailActivity"
        const val TRANSACTION_EXTRA = "TRANSACTION_EXTRA"
        const val TRANSACTION_MTN = "TRANSACTION_MTN"
        const val TRANSACTION_OFFLINE = "TRANSACTION_OFFLINE"
        const val RETURN_INTENT_DATA = "RETURN_INTENT_DATA"
    }

    private val cancelTimer = SWTimer()
    private val swTimerFragmentLifecycleCallback = SWTimerActivityLifecycleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application.registerActivityLifecycleCallbacks(swTimerFragmentLifecycleCallback)

        setContent {
            TransactionStatusDetailLayout(
                cancelTimer = cancelTimer,
                registerEventCallBack = { registerEvent(it) },
                onBackActionCallback = { onBackPressedDispatcher.onBackPressed() }
            )
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back")
                    finish()
                }
            },
        )

        cancelTimer.cancel()
    }

    override fun getSwTimer() = cancelTimer

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                eventCategory = ScreenCategory.DASHBOARD.value,
                eventAction = eventAction,
                hierarchy = getHierarchy("")
            )
        )
    }
}
