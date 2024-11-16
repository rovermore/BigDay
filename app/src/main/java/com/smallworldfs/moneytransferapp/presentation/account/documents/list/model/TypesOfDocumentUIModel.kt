package com.smallworldfs.moneytransferapp.presentation.account.documents.list.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypesOfDocumentUIModel(
    val id: String = STRING_EMPTY,
    val name: String = STRING_EMPTY,
) : Parcelable
