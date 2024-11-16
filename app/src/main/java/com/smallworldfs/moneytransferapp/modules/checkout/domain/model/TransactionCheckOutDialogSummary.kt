package com.smallworldfs.moneytransferapp.modules.checkout.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionCheckOutDialogSummary(
    var exchange_rate: RowModel,
    var footer: String,
    var receipt: Receipt,
    var sender: Sender
) : Parcelable {

    @Parcelize
    data class Receipt(
        var amount: RowModel,
        var fee: RowModel,
        var total: RowModel
    ) : Parcelable

    @Parcelize
    data class Sender(
        var amount: RowModel,
        var fee: RowModel,
        var taxes: RowModel,
        var total: RowModel
    ) : Parcelable

    @Parcelize
    data class RowModel(
        var label: String,
        var value: String
    ) : Parcelable
}
