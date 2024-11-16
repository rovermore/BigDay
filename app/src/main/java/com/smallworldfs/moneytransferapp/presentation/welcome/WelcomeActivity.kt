package com.smallworldfs.moneytransferapp.presentation.welcome

import android.os.Bundle
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : GenericActivity() {

    companion object {
        const val PASSCODE_DIALOG_KEY = "PASSCODE_ACTIVITY"
        const val LOGIN = "LOGIN"
    }

    @Inject
    lateinit var navigator: WelcomeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeLayout(navigator) { event ->
                registerEvent(event)
            }
        }
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                eventCategory = ScreenCategory.ONBOARDING.value,
                eventAction = eventAction,
                hierarchy = getHierarchy(""),
            ),
        )
    }
}
