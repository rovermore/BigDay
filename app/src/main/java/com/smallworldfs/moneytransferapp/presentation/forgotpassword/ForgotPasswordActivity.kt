package com.smallworldfs.moneytransferapp.presentation.forgotpassword

import android.os.Bundle
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity

class ForgotPasswordActivity : GenericActivity() {

    companion object {
        const val ONE_SECOND = 3600
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotPasswordLayout(
                object : ForgotPasswordListener {
                    override fun registerEventCallback(eventAction: String, eventLabel: String, formType: String) {
                        registerEvent(eventAction, eventLabel, formType)
                    }

                    override fun onBackAction() {
                        finish()
                    }

                    override fun trackScreenName(screenName: String) {
                        trackScreen(screenName)
                    }
                }
            )
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String, formType: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
            ),
        )
    }
}
