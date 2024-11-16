package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
class CountriesData(
    val countries: MutableList<CountryData> = mutableListOf()
) : Parcelable

@Parcelize
data class CountryData(
    val iso3: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val isoPhoneCode: String = STRING_EMPTY,
    val url: String = STRING_EMPTY
) : Parcelable
