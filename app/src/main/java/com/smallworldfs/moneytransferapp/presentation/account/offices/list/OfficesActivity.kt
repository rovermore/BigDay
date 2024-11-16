package com.smallworldfs.moneytransferapp.presentation.account.offices.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfficesActivity : GenericActivity() {

    @Inject
    lateinit var navigator: OfficesNavigator

    private val viewModel: OfficesViewModel by viewModels()

    private val startCountrySelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                viewModel.setSelectedCountry(result.key, result.value)
            }
        }

    private val startCitySelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                viewModel.filterByCity(result.key)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfficesActivityLayout(
                onCountrySelectorClick = { setCountrySelector(it) },
                onCitySelectorClick = { setCitySelector(it) },
                onOfficeClick = { navigator.navigateToOfficesMapActivity(it) },
                registerEventCallback = { registerEvent(it) },
                onBackPressed = { finish() },
            )
        }
    }

    private fun setCountrySelector(countryList: List<CountryUIModel>) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        countryList.forEach {
            listToShow.add(FormSelectorItem(it.iso3, it.name, urlDrawable = Constants.COUNTRY.FLAG_IMAGE_ASSETS + it.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION))
        }
        listToShow.sortBy { it.value }
        listToShow.add(INT_ZERO, FormSelectorItem(STRING_EMPTY, getString(R.string.offices_all_countries), drawable = R.drawable.account_icn_selectcountry, scaleType = ImageView.ScaleType.CENTER))
        val state = FormSelectorState(getString(R.string.offices_country_title), listToShow, true)
        val i = Intent(this, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        startCountrySelector.launch(i)
    }

    private fun setCitySelector(cityList: List<String>) {
        val listToShow: MutableList<FormSelectorItem> = mutableListOf()
        cityList.forEach {
            listToShow.add(FormSelectorItem(it, it))
        }
        listToShow.add(INT_ZERO, FormSelectorItem(STRING_EMPTY, getString(R.string.offices_all_cities)))
        val state = FormSelectorState(getString(R.string.offices_city_title), listToShow, screenName = ScreenName.SEARCH_OFFICE_LOCATION_SCREEN.value)
        val i = Intent(this, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        startCitySelector.launch(i)
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(""),
            ),
        )
    }
}
