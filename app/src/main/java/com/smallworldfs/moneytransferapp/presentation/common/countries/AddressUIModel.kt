package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddressUIModel(
    val id: String,
    val type: String,
    val detail: Detail
) : Parcelable {
    @Parcelize
    data class Detail(
        val text: String,
        val description: String
    ) : Parcelable
}
