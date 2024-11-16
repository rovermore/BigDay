package com.smallworldfs.moneytransferapp.presentation.account.contact

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.freshchat.consumer.sdk.Freshchat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel
import com.smallworldfs.moneytransferapp.modules.support.navigator.SelectContactSupportNavigator
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectContactSupportActivity : GenericActivity() {

    @Inject
    lateinit var navigator: SelectContactSupportNavigator

    private val selectContactSupportListener = object : SelectContactSupportListener {
        override fun onFaqsClick() {
            registerEvent("click_faqs")
            navigator.navigateToFaqs(getString(R.string.contact_faqs_url))
        }

        override fun onChatClick() {
            registerEvent("click_chat")
            Freshchat.showConversations(applicationContext)
            trackScreen(ScreenName.CUSTOMER_SERVICE_CHAT_SCREEN.value)
        }

        override fun onEmailClick(userUIModel: UserUIModel, contactSupportInfoUIModel: ContactSupportInfoUIModel) {
            registerEvent("click_email_us_your_inquiry")
            if (userUIModel.isLimitedUser()) {
                navigator.navigateToLimitedSendEmail(contactSupportInfoUIModel)
            } else {
                navigator.navigateToSendEmail(contactSupportInfoUIModel)
            }
        }

        override fun onBackPressed() {
            backPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SelectContactSupportLayout(listener = selectContactSupportListener)
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            },
        )
    }

    private fun backPressed() {
        registerEvent("click_back")
        finish()
    }

    private fun registerEvent(eventAction: String, eventLabel: String = STRING_EMPTY, formType: String = STRING_EMPTY) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.CONTACT.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
                STRING_EMPTY,
                STRING_EMPTY,
            ),
        )
    }
}
