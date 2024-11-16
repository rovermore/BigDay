package com.smallworldfs.moneytransferapp.presentation.common.countries

import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment.ChooseCountryFromFragment

interface LegacyCountrySelectedListener : ChooseCountryFromFragment.CountrySelectedListener {
    override fun onCountrySelected(country: Pair<String, String>?) {}
}
