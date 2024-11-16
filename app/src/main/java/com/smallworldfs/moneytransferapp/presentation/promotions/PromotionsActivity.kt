package com.smallworldfs.moneytransferapp.presentation.promotions

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.promotions.listener.PromotionsActivityListener
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class PromotionsActivity : GenericActivity() {

    companion object {
        const val PAYOUT_COUNTRY = STRING_EMPTY
    }

    private val viewModel: PromotionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadInformation(intent?.getStringExtra(PAYOUT_COUNTRY) ?: STRING_EMPTY)

        setContent {
            PromotionsLayout(
                listener = object : PromotionsActivityListener {
                    override fun onBackPressed() {
                        registerEventCallback("click_back", STRING_EMPTY)
                        finish()
                    }

                    override fun onDoneAction() {
                        finish()
                    }

                    override fun registerEventCallback(eventAction: String, eventLabel: String) {
                        registerEvent(eventAction, eventLabel)
                    }

                    override fun registerBrazeEventCallback(eventName: String, eventProperties: Map<String, String>) {
                        registerBrazeEvent(eventName, eventProperties)
                    }
                },
            )
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.TRANSFER.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                "",
                "bank_deposit",
                "",
                "",
                "",
                "",
            ),
        )
    }

    private fun registerBrazeEvent(eventName: String, eventProperties: Map<String, String>) {
        trackEvent(
            BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION),
        )
    }
}
