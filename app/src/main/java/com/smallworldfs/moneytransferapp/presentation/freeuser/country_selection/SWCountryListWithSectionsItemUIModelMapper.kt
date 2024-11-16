package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import javax.inject.Inject

class SWCountryListWithSectionsItemUIModelMapper @Inject constructor() {
    fun mapToItem(countryUIModel: CountryUIModel) = SWCountryListWithSectionsItemUIModel.Item(country = countryUIModel)
    fun mapToLastUsedHeader() = SWCountryListWithSectionsItemUIModel.Header(title = R.string.previously_selected)
    fun mapToAllCountriesHeader() = SWCountryListWithSectionsItemUIModel.Header(title = R.string.select_new)
    fun mapToItem(countries: List<CountryUIModel>) = countries.map { mapToItem(it) }
}
