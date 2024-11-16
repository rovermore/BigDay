package com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity

import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

interface HomeActivityCountrySelectionCallback {

    fun originSelection(countryUIModel: CountryUIModel)
    fun destinationSelection(countryUIModel: CountryUIModel)
}
