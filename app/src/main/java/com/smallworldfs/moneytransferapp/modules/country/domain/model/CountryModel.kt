package com.smallworldfs.moneytransferapp.modules.country.domain.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class CountryModel(
    var countryKey: String = STRING_EMPTY,
    var countryValue: String = STRING_EMPTY
)
