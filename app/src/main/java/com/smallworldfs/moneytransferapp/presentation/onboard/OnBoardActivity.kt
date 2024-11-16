package com.smallworldfs.moneytransferapp.presentation.onboard

import android.os.Bundle
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_TWO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnBoardLayout(
                onButtonClicked = { page ->
                    registerEvent(page)
                },
                onActionPageSelected = { page ->
                    when (page) {
                        INT_ZERO -> trackScreen(ScreenName.INFO_STRESS_FREE_SCREEN.value)
                        INT_ONE -> trackScreen(ScreenName.INFO_SECURE_SCREEN.value)
                        INT_TWO -> trackScreen(ScreenName.INFO_REAL_PEOPLE_SCREEN.value)
                    }
                }
            )
        }
    }

    private fun registerEvent(position: Int) {
        val hierarchy = when (position) {
            INT_ZERO -> ScreenName.INFO_STRESS_FREE_SCREEN.value
            INT_ONE -> ScreenName.INFO_SECURE_SCREEN.value
            INT_TWO -> ScreenName.INFO_REAL_PEOPLE_SCREEN.value
            else -> STRING_EMPTY
        }
        trackEvent(
            UserActionEvent(
                ScreenCategory.ONBOARDING.value,
                "click_start",
                "wizard",
                getHierarchy(hierarchy),
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            ),
        )
    }
}
