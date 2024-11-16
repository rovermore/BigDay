package com.smallworldfs.moneytransferapp.presentation.softregister.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class RegisterStep : Parcelable {
    @Parcelize object RegisterCredentials : RegisterStep()
    @Parcelize object RegisterPhone : RegisterStep()
    @Parcelize object RegisterData : RegisterStep()
}
