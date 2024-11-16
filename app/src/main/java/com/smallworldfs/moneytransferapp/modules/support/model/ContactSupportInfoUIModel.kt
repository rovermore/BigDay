package com.smallworldfs.moneytransferapp.modules.support.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactSupportInfoUIModel(
    val phone: String = STRING_EMPTY,
    val email: String = STRING_EMPTY,
    val isLimitedUser: Boolean = false
) : Parcelable
