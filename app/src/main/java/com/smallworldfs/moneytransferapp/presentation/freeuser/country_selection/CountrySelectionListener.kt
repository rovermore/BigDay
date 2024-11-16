package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

interface CountrySelectionListener {
    fun onOriginSearchClick(eventName: String, hierarchy: String)
    fun onDestinationSearchClick(eventName: String, hierarchy: String)
    fun onOriginCountrySelected(country: CountryUIModel)
    fun onDestinationCountrySelected(country: CountryUIModel)
    fun onContinueButtonClicked(eventLabel: String, origin: String)
    fun onFreeUserCreated(user: UserDTO, originCountry: CountryUIModel, destinationCountry: CountryUIModel)
    fun trackSendingFromScreen()
    fun trackSendingToScreen()
    fun dismissBottomSheetEvent(eventAction: String, hierarchy: String)
}
