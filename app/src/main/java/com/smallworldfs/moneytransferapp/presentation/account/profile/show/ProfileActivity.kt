package com.smallworldfs.moneytransferapp.presentation.account.profile.show

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileLayout(
                listener = object : ProfileLayoutListener {
                    override fun registerEventCallBack(eventName: String) {
                        registerEvent(eventName)
                    }

                    override fun onBackAction() {
                        onActivityBackAction()
                    }
                },
            )
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onActivityBackAction()
                }
            },
        )
    }

    private fun onActivityBackAction() {
        registerEvent("click_back")
        finish()
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(STRING_EMPTY),
            ),
        )
    }
}
