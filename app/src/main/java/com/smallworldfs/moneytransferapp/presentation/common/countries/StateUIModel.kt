package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StateUIModel(
    val code: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
    val logo: String = STRING_EMPTY,
    val active: Boolean = false,
) : Parcelable
