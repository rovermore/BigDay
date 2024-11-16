package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

interface CountrySelectionContentCallbacks {
    fun onOriginCountrySelected(country: CountryUIModel)
    fun onDestinationCountrySelected(country: CountryUIModel)
    fun onStepChanged()
    fun onQueryOriginChanged(query: String)
    fun onQueryDestinationChanged(query: String)
    fun onOriginSearchClicked()
    fun onDestinationSearchClicked()
    fun onFreeUserCreated(user: UserDTO, originCountry: CountryUIModel, destinationCountry: CountryUIModel)
    fun trackSendingFromScreen()
    fun trackSendingToScreen()
    fun dismissBottomSheetEvent(eventAction: String, hierarchy: String)
}
