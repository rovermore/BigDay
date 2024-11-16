package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.freshchat.consumer.sdk.Freshchat
import com.google.firebase.messaging.FirebaseMessaging
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.account.FreeUserNavigator
import com.smallworldfs.moneytransferapp.utils.Log
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountrySelectionActivity : GenericActivity() {

    @Inject
    lateinit var navigator: FreeUserNavigator

    private var currentScreen: String = STRING_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountrySelectionLayout(
                countrySelectionListener = object : CountrySelectionListener {
                    override fun onOriginSearchClick(eventName: String, hierarchy: String) {
                        registerEvent(
                            eventAction = "search_sending_from",
                            formType = "search",
                            hierarchy = ScreenName.SENDING_FROM_SCREEN.value,
                        )
                    }

                    override fun onDestinationSearchClick(eventName: String, hierarchy: String) {
                        registerEvent(
                            eventAction = "search_sending_to",
                            formType = "search",
                            hierarchy = ScreenName.SENDING_TO_SCREEN.value,
                        )
                    }

                    override fun onOriginCountrySelected(country: CountryUIModel) {
                        registerEvent("click_sending_from_list", country.iso3)
                    }

                    override fun onDestinationCountrySelected(country: CountryUIModel) {
                        registerEvent("click_sending_to_list", country.iso3)
                    }

                    override fun onContinueButtonClicked(eventLabel: String, origin: String) {
                        registerEvent(
                            eventAction = "click_continue",
                        )
                        registerEvent(
                            eventAction = "completed_unreg_form",
                            eventLabel = eventLabel,
                            origin = origin,
                        )
                    }

                    override fun onFreeUserCreated(user: UserDTO, originCountry: CountryUIModel, destinationCountry: CountryUIModel) {
                        setupFreshChat(user)
                        val properties = HashMap<String, String>()

                        properties[BrazeEventProperty.REGISTRATION_SENDING_COUNTRY.value] = originCountry.iso3
                        properties[BrazeEventProperty.REGISTRATION_DESTINATION_COUNTRY.value] = destinationCountry.iso3

                        registerBrazeEvent(BrazeEventName.REGISTRATION_DESTINATION.value, properties)
                        navigator.navigateToHome()
                    }

                    override fun trackSendingFromScreen() {
                        currentScreen = ScreenName.SENDING_FROM_SCREEN.value
                        trackScreen(ScreenName.SENDING_FROM_SCREEN.value)
                    }

                    override fun trackSendingToScreen() {
                        currentScreen = ScreenName.SENDING_TO_SCREEN.value
                        trackScreen(ScreenName.SENDING_TO_SCREEN.value)
                    }

                    override fun dismissBottomSheetEvent(eventAction: String, hierarchy: String) {
                        registerEvent(eventAction, hierarchy)
                    }
                },
            )
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent(
                        eventAction = ScreenCategory.WELCOME.value,
                        eventLabel = when (currentScreen) {
                            ScreenName.SENDING_FROM_SCREEN.value -> "click_back_send_money_from"
                            ScreenName.SENDING_TO_SCREEN.value -> "click_back_send_money_to"
                            else -> STRING_EMPTY
                        },
                    )
                    finish()
                }
            },
        )
    }

    private fun setupFreshChat(user: UserDTO) {
        try {
            Freshchat.resetUser(this)
            with(Freshchat.getInstance(this)) {
                identifyUser(user.id, user.freshchatId)
                this.user.firstName = user.name
                this.user.lastName = user.surname
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isNullOrEmpty()) {
                        this.setPushRegistrationToken(task.result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("FRESHCHAT_ERROR", "FreshChat user not set")
        }
    }

    private fun registerEvent(
        eventAction: String,
        eventLabel: String = STRING_EMPTY,
        hierarchy: String = STRING_EMPTY,
        formType: String = STRING_EMPTY,
        origin: String = STRING_EMPTY
    ) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.WELCOME.value,
                eventAction,
                eventLabel,
                getHierarchy(hierarchy),
                formType,
                STRING_EMPTY,
                STRING_EMPTY,
                origin,
                STRING_EMPTY,
            ),
        )
    }

    private fun registerBrazeEvent(eventName: String, eventProperties: Map<String, String>) {
        trackEvent(
            BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION),
        )
    }
}
