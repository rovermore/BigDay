package com.smallworldfs.moneytransferapp.compose.widgets.model

import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

sealed class SWCountryListWithSectionsItemUIModel {

    class Header(
        val title: Int
    ) : SWCountryListWithSectionsItemUIModel()

    class Item(
        val country: CountryUIModel
    ) : SWCountryListWithSectionsItemUIModel()

    object EmptyItem : SWCountryListWithSectionsItemUIModel()
}
