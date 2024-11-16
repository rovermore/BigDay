package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity.Companion.SEND_MONEY_FROM
import com.smallworldfs.moneytransferapp.presentation.softregister.phone.RegisterPhoneFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCountryActivity : GenericActivity() {

    private val viewModel: SearchCountryViewModel by viewModels()

    companion object {
        const val ANALITYCS_TAG = "ANALITYCS_TAG"
        const val SELECTED_COUNTRY_KEY = "SELECTED"
        const val TITLE_KEY = "TITLE"
        const val TYPE = "TYPE"
        const val COUNTRIES_KEY = "COUNTRIES"
        const val SEPARATOR_INDEX = "SEPARATOR_INDEX"
        private const val ONE_SECOND = 1000L
    }

    private var lastTimeEventSend = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countries: ArrayList<CountryUIModel>? = intent.getParcelableArrayListExtra(COUNTRIES_KEY)

        val barTitle = intent.getStringExtra(TITLE_KEY) ?: resources.getString(R.string.offices_country_title)

        setContent {
            SearchCountry(
                onItemClicked = { returnResult(it) },
                onValueChange = { onSearchTextChanged(it) },
                countryList = countries ?: emptyList(),
                onBackPressed = { onBackClicked() },
                barTitle = barTitle
            )
        }

        if (countries.isNullOrEmpty()) viewModel.getDestinationCountries()
    }

    private fun onBackClicked() {
        if (intent.getStringExtra(TYPE) == SEND_MONEY_FROM) {
            registerEvent(ScreenCategory.ACCESS.value, "click_back_send_money_from", hierarchy = ScreenName.SEND_MONEY_FROM_SCREEN.value)
        } else if (intent.getStringExtra(TYPE) == RegisterPhoneFragment.PHONE_CODE_VIEW) {
            registerEvent(ScreenCategory.ACCESS.value, "click_back_country_calling_code", hierarchy = ScreenName.COUNTRY_CALLING_CODE.value)
        }
        finish()
    }

    private fun onSearchTextChanged(text: String) {
        if (text.isNotBlank()) {
            if (text.length > 2 && (System.currentTimeMillis() - lastTimeEventSend) > ONE_SECOND) {
                if (intent.getStringExtra(TYPE) == SEND_MONEY_FROM) {
                    registerEvent(ScreenCategory.ACCESS.value, "search_send_money_from", text, "search", hierarchy = ScreenName.SEND_MONEY_FROM_SCREEN.value)
                } else if (intent.getStringExtra(TYPE) == RegisterPhoneFragment.PHONE_CODE_VIEW) {
                    registerEvent(ScreenCategory.ACCESS.value, "search_country_calling_code", text, "search", hierarchy = ScreenName.COUNTRY_CALLING_CODE.value)
                }
                lastTimeEventSend = System.currentTimeMillis()
            }
        }
    }

    private fun returnResult(country: CountryUIModel) {
        val intent = Intent().apply {
            putExtra(SELECTED_COUNTRY_KEY, country)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun registerEvent(screenCategory: String, eventAction: String, eventLabel: String = "", formType: String = "", hierarchy: String = "") {
        trackEvent(
            UserActionEvent(
                screenCategory,
                eventAction,
                eventLabel,
                getHierarchy(hierarchy),
                formType,
            ),
        )
    }
}
