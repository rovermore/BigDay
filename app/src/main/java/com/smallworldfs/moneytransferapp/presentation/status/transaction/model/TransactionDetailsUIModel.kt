package com.smallworldfs.moneytransferapp.presentation.status.transaction.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDetailsUIModel(
    val transaction: TransactionUIModel = TransactionUIModel(),
    val cancellationMessage: String = STRING_EMPTY,
) : Parcelable
