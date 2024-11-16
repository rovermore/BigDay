package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryUIModel(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val logo: String = STRING_EMPTY,
    val prefix: String = STRING_EMPTY,
    val countryCode: String = STRING_EMPTY,
    val featured: Boolean = false,
) : Parcelable
